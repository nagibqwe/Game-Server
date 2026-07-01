package common.welfare;

import com.data.*;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_RetrieveRes_Bean;
import com.data.bean.Cfg_VipPower_Bean;
import com.data.bean.Cfg_Vip_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.game.vip.structs.VipPower;
import com.game.welfare.script.IRetrieveResScript;
import com.game.welfare.struct.RetrieveResData;
import com.game.welfare.struct.RetrieveType;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2020/1/6 17:43.
 * @author: tc
 */
public class RetrieveResScript implements IRetrieveResScript {
    private final Logger log = LogManager.getLogger(RetrieveResScript.class);

    @Override
    public int getId() {
        return ScriptEnum.RetrieveResScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }



    private boolean checkFindLimtTimeActivity(Player player,RetrieveResData data,int type){
        Cfg_RetrieveRes_Bean bean = cfg(type, player.getLevel());
        Cfg_Daily_Bean bean1 = CfgManager.getCfg_Daily_Container().getValueByKey(bean.getDailyId());
        if (bean1 == null){
            log.error("bean.getDailyId()  not find: {}",bean.getDailyId());
            return false;
        }
        //策划强制改为0点刷新
       // long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        int curWeekDay = TimeUtils.getDayOfWeek( data.getTime());

        for (int weekDay : bean1.getOpenTime().getValue()) {
            if (weekDay == 0) {
                return true;
            }
            if (weekDay == curWeekDay) {
                return true;
            }
        }
        return false;
    }
    /**
     * 请求找回资源
     * @param type   资源找回类型
     * @param rrType 0完美找回， 1部分找回
     * @param findCount
     * @param isOnkey
     */
    @Override
    public int onReqRetrieveRes(Player player, int type, int rrType, int findCount, boolean isOnkey) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack))
            return 0;

        if (findCount <= 0){
            return 1;
        }
        checkSwitchDay(player, true);
        RetrieveResData data = player.getLastRRD();
        long now = TimeUtils.Time();
        //策划强制改为0点刷新
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        if (!TimeUtils.isSameDay(now - GlobalType.MILLIS_PER_DAY , data.getTime() )) {
            log.error(player.getInfo() + " now:" + now + " dt:" + data.getTime() + " nows:" + TimeUtils.format2string(now) + " dts:" + TimeUtils.format2string(data.getTime()));
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GetContentFailed);
            return 0;
        }

        RetrieveType rt = RetrieveType.type(type);
        if (RetrieveType.None.compare(rt))
            return 1;

        if (!data.getOpenType().contains(rt.type())) {
            log.info("功能未开放！");
            return 1;
        }

        Cfg_RetrieveRes_Bean bean = cfg(type, player.getLevel());
        if (!canRR(bean, player))
            return 1;

        if (!checkFindLimtTimeActivity(player,data,type)){
            log.info("该天没有限时活动举行  {} ",type);
            return 1;
        }

        if (data.getVipLevel() <=0){
            data.setVipLevel(player.getVipLv());
        }
        int vipMaxTimes = getVipMaxCount( bean.getVipPower(),data.getVipLevel());
        long count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
        long vipCount = count(rt,data.getVipMap().getOrDefault(rt.type(),0),vipMaxTimes);
        long allCount = count  + vipCount;
        if (allCount <= 0){
            log.info("没有可找回的次数 {}",type);
            return 1;
        }
        if (findCount  > allCount){
            log.info("找回次数大于总次数  all {}  findCount  {}",allCount,findCount);
            return 1;
        }

        long buyVipCount = findCount > count ? findCount - count:0;
        int alreadyBuyCount =  data.getVipMap().getOrDefault(rt.type(),0);
        int needBuyVipgold = 0;
        if (buyVipCount > 0){
           for (int i = 0;i<buyVipCount;i++){
               needBuyVipgold += Manager.vipManager.power().getVipAddNumPrice(alreadyBuyCount + i + 1, bean.getVipPower());
           }
            if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, needBuyVipgold)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.GoldNotEnough);
                log.info("元宝不足 不能找回  needgold {} type {}",needBuyVipgold,type);
                return 1;
            }
        }

        long ber = findCount;
        ReadIntegerArray cost;
        ReadLongArrayEs reward;
        long addExp = 0 ;
        if (rrType == 0) {
            cost = bean.getCost_perfect();
            reward = bean.getReward_perfect();
            addExp = bean.getRewardexp_perfect();
        } else {
            cost = bean.getCost_part();
            reward = bean.getReward_part();
            addExp = bean.getRewardexp_part();
        }
        int findActivePoint = 0;
        int hasFindActivePoint = data.getHasActivePoint();
        List<ReadArray<Long>> readArraylist = new ArrayList<>();
        for (ReadArray<Long> array : reward.getValuees()){
            if (array.get(0) != ItemCoinType.ActivePoint){
                readArraylist.add(array);
            }else{
                findActivePoint =  array.get(1).intValue() * (int) ber;
            }
        }
        reward =  Utils.toReadLongArrayEsByArray(readArraylist);

        if (cost == null || cost.size() != 2)
            return 1;

        long costNum;
        int maxPoint = getDailyActiveMax(player);
        if (RetrieveType.Activity.compare(rt)) {
            if (rrType == 0)
                costNum = (long) (Global.RetrieveRes_Activity_Scale.get(0) * cost.get(1) * ber);
            else
                costNum = (long) (Global.RetrieveRes_Activity_Scale.get(1) * cost.get(1) * ber);
        } else {
            findActivePoint = hasFindActivePoint + findActivePoint > maxPoint ? maxPoint -  hasFindActivePoint : findActivePoint;
            costNum = cost.get(1) * ber;
        }
        if (cost.get(0) == ItemCoinType.BindGemCoin){
            // cost
            if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, costNum +needBuyVipgold)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(cost.get(0)));
                return 1;
            }
        }else {
            if (!Manager.currencyManager.manager().canDecItemCoin(player, costNum, cost.get(0))) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(cost.get(0)));
                return 1;
            }
        }


        List<Item> items = new ArrayList<>();
        if (!RetrieveType.Activity.compare(rt)) {
            items = Item.createItems(player.getCareer(), reward, (int) ber);
            int reason = Manager.backpackManager.manager().onHasAddSpaces(player, items);
            if (reason != 0) {
                MessageUtils.notify_player(player, Notify.ERROR, reason);
                return 1;
            }
        }

        long action = IDConfigUtil.getLogId();
        if (cost.get(0) == ItemCoinType.BindGemCoin){
            Manager.currencyManager.manager().decBindGoldOrGold(player, (int) costNum, ItemChangeReason.RetrieveResDec, action);
        }else {
            Manager.currencyManager.manager().onDecItemCoin(player, costNum, ItemChangeReason.RetrieveResDec, action, cost.get(0));
        }
        if (needBuyVipgold > 0){
            Manager.currencyManager.manager().decBindGoldOrGold(player, needBuyVipgold, ItemChangeReason.RetrieveResDec, action);
        }

        if (RetrieveType.Activity.compare(rt)) {
            long num2 = 0;
            if (rrType == 0)
                num2 = (long) (Global.RetrieveRes_Activity_Scale.get(0) * ber);
            else
                num2 = (long) (Global.RetrieveRes_Activity_Scale.get(1) * ber);
            count(data, rt, count - ber);
            // 添加活跃度货币
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.ActivePoint, num2, ItemChangeReason.RetrieveResAdd, action);
            Manager.countManager.addVariant(player, VariantType.Daily_Active_Value, num2);
        } else {
            count(data, rt, ber - buyVipCount);
            // 添加奖励
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.RetrieveResAdd, action);
            if (findActivePoint > 0){
                // 添加活跃度货币
                Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.ActivePoint, findActivePoint, ItemChangeReason.RetrieveResAdd, action);
            }
        }
        if (addExp > 0){
            Manager.currencyManager.manager().addEXP(player, addExp, ItemChangeReason.RetrieveResAdd, action);
        }
        data.getVipMap().put(rt.type(),alreadyBuyCount + (int) buyVipCount);
        data.setHasActivePoint(hasFindActivePoint + findActivePoint);
        log.info(player.getInfo() + " 找回资源成功 " + type + " 类型 " + rrType);


        if (!isOnkey){
            // sync client
            WelfareMessage.SyncRetrieveResOne.Builder msg = WelfareMessage.SyncRetrieveResOne.newBuilder();
            WelfareMessage.RetrieveRes.Builder rr = WelfareMessage.RetrieveRes.newBuilder();
            rr.setType(rt.type());
            count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
            long vipBuyAllCount = alreadyBuyCount +  buyVipCount;
            int vipLatCount =  (int) count(rt,vipBuyAllCount,vipMaxTimes);
            rr.setRemain(count);
            rr.setVipCountMax( vipMaxTimes);
            rr.setVipCount(vipLatCount);
            msg.setRes(rr);
            msg.setCanFindActivePoint(maxPoint - data.getHasActivePoint());
            MessageUtils.send_to_player(player, WelfareMessage.SyncRetrieveResOne.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        if(rrType == 0){
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.RetrieveRes, ItemChangeReason.RetrieveResAdd, type);
        }else{
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.RetrieveRes, ItemChangeReason.RetrieveResPartAdd, type);
        }
        return 2;
    }

    /**
     * // 请求一键找回
     * @param player
     * rrType 0完美找回， 1部分找回
     * baseTpe 0全部找回，1基础找回
     */
    @Override
    public void onReqOneKeyRetrieveRes(Player player, int rrType, int baseTpe) {
        RetrieveResData data = player.getLastRRD();
        long now = TimeUtils.Time();
        //策划强制改为0点刷新
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        boolean have = Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack) &&
                !TimeUtils.isSameDay(now , data.getTime() );
        if (have){
            WelfareMessage.SyncRetrieveResList.Builder msg = WelfareMessage.SyncRetrieveResList.newBuilder();
            for (RetrieveType rt : RetrieveType.values()) {

                if (!data.getOpenType().contains(rt.type())) {
                    continue;
                }

                Cfg_RetrieveRes_Bean bean = cfg(rt.type(), player.getLevel());
                if (!canRR(bean, player)){
                    continue;
                }

                if (!checkFindLimtTimeActivity(player,data,rt.type())){
                    continue;
                }

                if (data.getVipLevel() <=0){
                    data.setVipLevel(player.getVipLv());
                }
                long count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
                int vipMaxTimes = getVipMaxCount(bean.getVipPower(),data.getVipLevel());
                int vipHasBuyCount =data.getVipMap().getOrDefault(rt.type(),0);
                int vipCount = (int) count(rt,vipHasBuyCount,vipMaxTimes);
                if (count <= 0  && vipCount <=0)
                    continue;

                int findCount = (int)(baseTpe == 0?vipCount + count: count);
                if (onReqRetrieveRes(player,rt.type(),rrType,findCount,true) == 0){
                    log.error("功能未开放或找回时间没达到");
                    return;
                }

                //TODO 找回之后从新计算次数 返回客户端
                 count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
                 vipHasBuyCount =data.getVipMap().getOrDefault(rt.type(),0);
                 vipCount = (int) count(rt,vipHasBuyCount,vipMaxTimes);
                 if (count <= 0  && vipCount <=0)
                    continue;
                 WelfareMessage.RetrieveRes.Builder rr = WelfareMessage.RetrieveRes.newBuilder();
                 rr.setType(rt.type());
                 rr.setRemain(count);
                 rr.setVipCount(vipCount);
                 rr.setVipCountMax(vipMaxTimes);
                 msg.addLists(rr);
            }
            int maxPoint = getDailyActiveMax(player);
            msg.setCanFindActivePoint(maxPoint - data.getHasActivePoint());
            MessageUtils.send_to_player(player, WelfareMessage.SyncRetrieveResList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 玩家上线
     * player
     */
    @Override
    public void online(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack))
            return;
        checkSwitchDay(player, false);
        syncRetrieveResList(player);
    }

    /**
     * 完成一次活动
     *
     * @param retrieveType 找回类型
     */
    @Override
    public void count(Player player, RetrieveType retrieveType) {
        count(player, retrieveType, 1);
    }

    /**
     * 完成count次活动
     *
     * @param retrieveType 找回类型
     * @param count        次数
     */
    @Override
    public void count(Player player, RetrieveType retrieveType, long count) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack))
            return;
        //checkSwitchDay(player, false);
        RetrieveResData data = player.getCurRRD();
        count(data, retrieveType, count);

    }

    private void count(RetrieveResData data, RetrieveType retrieveType, long count) {
        if (RetrieveType.Activity.compare(retrieveType)){
            data.getMap().put(retrieveType.type(), count);
        }
        else {
            long v = data.getMap().computeIfAbsent(retrieveType.type(), k -> 0L);
            data.getMap().put(retrieveType.type(), count + v);
        }
    }

    private void checkSwitchDay(Player player, boolean sync) {

        long now = TimeUtils.Time();
        //策划强制改为0点刷新
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        RetrieveResData data = player.getCurRRD();
        int days = TimeUtils.getBetweenDays(now , data.getTime() );
        if (days <= 0)
            return;
        else if (days == 1) {
            player.setLastRRD(player.getCurRRD());
        } else {
            // 全新
            RetrieveResData resData = new RetrieveResData(player.getVipLv());
            resData.setTime(now - GlobalType.MILLIS_PER_DAY);
            player.setLastRRD(resData);
            player.getLastRRD().setOpenType(player.getCurRRD().getOpenType());
        }
        player.setCurRRD(new RetrieveResData(player.getVipLv()));
        if (sync) {
            syncRetrieveResList(player);
        }
    }

    /**
     * 跨天
     */
    @Override
    public void switchDay(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack))
            return;

        player.getCurRRD().getOpenType().clear();
        for (RetrieveType rt : RetrieveType.values()) {
            Cfg_RetrieveRes_Bean bean = cfg(rt.type(), player.getLevel());
            if (canRR(bean, player)) {
                player.getCurRRD().getOpenType().add(rt.type());
            }
        }
        checkSwitchDay(player, true);
    }

    @Override
    public boolean canRetrieveRes(Player player) {
        RetrieveResData data = player.getLastRRD();
        long now = TimeUtils.Time();
        //策划强制改为0点刷新
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        boolean have = Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack) && !TimeUtils.isSameDay(now , data.getTime());
        if (have)
            for (RetrieveType rt : RetrieveType.values()) {
                if (!data.getOpenType().contains(rt.type())) {
                    continue;
                }

                Cfg_RetrieveRes_Bean bean = cfg(rt.type(), player.getLevel());
                if (!canRR(bean, player))
                    continue;

                long count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
                if (count <= 0)
                    continue;
                return true;
            }
        return false;
    }

    private void syncRetrieveResList(Player player) {
        WelfareMessage.SyncRetrieveResList.Builder msg = WelfareMessage.SyncRetrieveResList.newBuilder();
        RetrieveResData data = player.getLastRRD();
        long now = TimeUtils.Time();
        //策划强制改为0点刷新
        //long zeroOffset = Global.Daily_times_reset_time * GlobalType.MILLIS_PER_HOUR;
        boolean have = Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ResBack) && !TimeUtils.isSameDay(now, data.getTime());
        if (have)
            for (RetrieveType rt : RetrieveType.values()) {
                if (!data.getOpenType().contains(rt.type())) {
                    continue;
                }

                Cfg_RetrieveRes_Bean bean = cfg(rt.type(), player.getLevel());
                if (!canRR(bean, player))
                    continue;
                if (!checkFindLimtTimeActivity(player,data,rt.type())){
                    continue;
                }

                if (data.getVipLevel() <=0){
                    data.setVipLevel(player.getVipLv());
                }
                long count = count(rt, data.getMap().getOrDefault(rt.type(), 0L), bean.getMax());
                int vipMaxTimes =  getVipMaxCount( bean.getVipPower(),data.getVipLevel());
                int vipHasBuyCount =data.getVipMap().getOrDefault(rt.type(),0);
                int vipCount = (int) count(rt,vipHasBuyCount,vipMaxTimes);

                if (count <= 0  && vipCount <=0)
                    continue;
                WelfareMessage.RetrieveRes.Builder rr = WelfareMessage.RetrieveRes.newBuilder();
                rr.setType(rt.type());
                rr.setRemain(count);
                rr.setVipCount(vipCount);
                rr.setVipCountMax(vipMaxTimes);
                msg.addLists(rr);
            }
        int maxPoint = getDailyActiveMax(player);
        msg.setCanFindActivePoint(maxPoint - data.getHasActivePoint());
        MessageUtils.send_to_player(player, WelfareMessage.SyncRetrieveResList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private long count(RetrieveType rt, long count, int max) {
        if (RetrieveType.Activity.compare(rt))
            count = Math.min(count, max);
        else {
            count = max - count < 0 ? 0 : max - count;
            count = Math.max(0, (int) count);
        }
        return count;
    }

    private Cfg_RetrieveRes_Bean cfg(int type, int level) {
        for (Cfg_RetrieveRes_Bean bean : CfgManager.getCfg_RetrieveRes_Container().getValuees()) {
            if (bean.getType() != type) continue;
            if (level >= bean.getMinLevel() && level <= bean.getMaxLevel())
                return bean;
        }
        return null;
    }

    private boolean canRR(Cfg_RetrieveRes_Bean bean, Player player) {
        if (bean == null || player == null)
            return false;

        if (bean.getOpenVariables() == 0)
            return true;

        if (RetrieveType.GuildTask.compare(bean.getType()) && !player.isHaveGuild()) {
            return false;
        }
        if (RetrieveType.GuildBattle.compare(bean.getType()) && !player.isHaveGuild()) {
            return false;
        }
        if (RetrieveType.GuildBoss.compare(bean.getType()) && !player.isHaveGuild()) {
            return false;
        }
        return Manager.controlManager.deal().isOpenFunction(player, bean.getOpenVariables());
    }

    private int getDailyActiveMax(Player player) {
        Integer activeBaseMaxValue = 0;
        for (int i = 0; i < Global.EveryDay_Activity_Point_Max.size(); i++) {
            ReadArray<Integer> array = Global.EveryDay_Activity_Point_Max.get(i);
            if (player.getLevel() >= array.get(0)) {
                activeBaseMaxValue = array.get(1);
            }
        }
        activeBaseMaxValue += (int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxNum);
        activeBaseMaxValue += Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_9);
        return activeBaseMaxValue;
    }

    public void addVipBuyCount(Player player,int dailyID,int addnum){
        int retrieveType =  getRetrieveTypeByDaily(dailyID);
        if (retrieveType > 0 ){
            int retrieveNum =  player.getCurRRD().getVipMap().getOrDefault(retrieveType,0);
            player.getCurRRD().getVipMap().put(retrieveType,retrieveNum+addnum);
        }
    }

    public void onSendResourceFindChangeToGame(Player player,int type){
        CrossServerMessage.F2GResourceFindChange.Builder msg = CrossServerMessage.F2GResourceFindChange.newBuilder();
        msg.setRoleId(player.getId());
        msg.setType(type);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GResourceFindChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void onF2GResourceFindChange(long roleId,int type){
        Player player =  Manager.playerManager.getPlayerCache(roleId);
        if (player == null){
            log.info("player  == null  {}",roleId);
            return;
        }
        for (RetrieveType rt : RetrieveType.values()) {
            if (rt.type() != type) {
                continue;
            }
            count(player,rt);
        }
    }


    private int getRetrieveTypeByDaily(int dailyId){
        int retrieveType = 0;
        DailyActiveDefine dailyActiveDefine = DailyActiveDefine.find(dailyId);
        if (dailyActiveDefine == null) {
            return retrieveType;
        }
        switch (dailyActiveDefine) {
            case JJC:
                retrieveType = RetrieveType.JJC.type();
                break;
            case ExpCopy:
                retrieveType = RetrieveType.ExpCopy.type();
                break;
            case EquipCopy:
                retrieveType = RetrieveType.XinMoCopy.type();
                break;
            case StrengthenCopy:
                retrieveType = RetrieveType.FiveCopy.type();
                break;
            case WORLD_BOSS:
                retrieveType =  RetrieveType.WorldBoss.type();
                break;
            case SUIT_BOSS:
                retrieveType = RetrieveType.SuitBoss.type();
                break;
            case GemBoss:
                retrieveType = RetrieveType.GemBoss.type();
                break;
            case StateBoss:
                retrieveType = RetrieveType.StateBoss.type();
                break;
            case FairyLand:
                retrieveType = RetrieveType.FairyLand.type();
                break;
            case HOME_BOSS:
                retrieveType = RetrieveType.VipBoss.type();
                break;
            default:
                break;
//                logger.error("未找到日常对应的vipPowerType:" + dailyId);
        }

        return retrieveType;
    }

    private  int getVipMaxCount( int type,int vipLv) {
        Cfg_VipPower_Bean bean = CfgManager.getCfg_VipPower_Container().getValueByKey(type);
        if (bean == null || bean.getVipPowerPrice() == null || bean.getVipPowerPrice().size() == 0) {
            return 0;
        }
        int num = 0;
        if (vipLv != 0) {
            Cfg_Vip_Bean vipBean = CfgManager.getCfg_Vip_Container().getValueByKey(vipLv);
            if (vipBean != null && vipBean.getVipPowerId().contains(type)) {
                for (ReadArray<Integer> r1 : vipBean.getVipPowerPra().getValuees()) {
                    if (r1.get(0) == type) {
                        num += r1.get(1);
                    }
                }
            }
        }
        return num;
    }
}
