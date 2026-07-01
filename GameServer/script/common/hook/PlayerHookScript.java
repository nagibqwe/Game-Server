package common.hook;

import com.data.*;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_On_hook_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.backpack.structs.ItemEffectType;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.ExpNoteData;
import com.game.copymap.structs.ZoneCache;
import com.game.count.structs.VariantType;
import com.game.equip.struct.EquipDefine;
import com.game.hook.log.HookLog;
import com.game.hook.manager.PlayerHookManager;
import com.game.hook.script.IPlayerHookScript;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.setting.struct.SettingType;
import com.game.structs.AttributeType;
import com.game.structs.EntityState;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.HookMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gsj
 */
public class PlayerHookScript implements IPlayerHookScript {

    private static final Logger log = LogManager.getLogger(PlayerHookScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerHookBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 请求挂机设置信息
     */
    @Override
    public void onReqHookSetInfoHandler(Player player) {
        int hookTime = player.getHookInfo().getHookTime();
        if (hookTime < 0) {
            player.getHookInfo().setHookTime(0);
            hookTime = 0;
        }
        int teamRate = Manager.backpackManager.manager().calTeamRate(player);
//        int vipRate = VipManager.getVipExpRate(player);
        int vipRate = 0;//屏蔽，消息也不发
        int worldLvRate = Manager.backpackManager.manager().calWorldLvRate(player);
        int itemRate = getCurrentItemRate(player);
        int amuletRate = Manager.godBookManager.deal().getAmuletExpRate(player);
        BaseIntAttribute attr = Manager.playerAttAttributeManager.deal().getAttribute(player, PlayerAttributeType.StifleFabao);
        int fabaoRate = attr.getAdditionValue(AttributeType.ATTR_MonserExp) / 100;
        long total = Manager.countManager.getVariant(player, VariantType.RechargeMoney);
        int firstRechargeRate = total >= Global.First_Recharge_Exp_Add ? Global.First_Rechar_Exp_Add / 100 : 0;
        long hookRate = getHookRate(player.getId());

        sendHookSetInfo(player, hookTime, hookRate, itemRate, vipRate, worldLvRate, teamRate, amuletRate, fabaoRate, firstRechargeRate);

        Manager.playerManager.manager().syncPlayerWorldInfo(player, false);
    }

    private void sendHookSetInfo(Player player, int hookRemainTime, long hookExpAddRate, int itemExpAddRate, int vipExpAddRate,
                                 int worldLvExpAddRate, int teamRate, int amuletRate, int fabaoRate, int firstRechargeRate) {
        HookMessage.ResHookSetInfo.Builder builder = HookMessage.ResHookSetInfo.newBuilder();
        builder.setHookRemainTime(hookRemainTime);
        builder.setHookExpAddRate(hookExpAddRate);
        builder.setFightExpAddRate(0);
        addHookRate(builder, PlayerHookManager.ItemRate, itemExpAddRate);
        addHookRate(builder, PlayerHookManager.WorldLvRate, worldLvExpAddRate);
//        addHookRate(builder, PlayerHookManager.VipRate, vipExpAddRate);
        addHookRate(builder, PlayerHookManager.TeamRate, teamRate);
        addHookRate(builder, PlayerHookManager.AmuletRate, amuletRate);
        addHookRate(builder, PlayerHookManager.FaBaoRate, fabaoRate);
        long total = Manager.countManager.getVariant(player, VariantType.RechargeMoney);

        if (total >= Global.First_Recharge_Exp_Add) {
            addHookRate(builder, PlayerHookManager.FcRate, firstRechargeRate);
        }
        if (player.getMarriageUid() > 0) {
            int addExp = Manager.backpackManager.manager().calMarriageRate(player);
            if (addExp > 0) {
                addHookRate(builder, PlayerHookManager.MarriageRate, addExp);
            }
        }
        int szRate = Manager.equipManager.deal().calEquipRate(player, EquipDefine.EquipPart_Bracelet);
        int ehRate = Manager.equipManager.deal().calEquipRate(player, EquipDefine.EquipPart_Earring);
        addHookRate(builder,PlayerHookManager.ShouZhuo,szRate);
        addHookRate(builder,PlayerHookManager.EerHuan,ehRate);
        builder.setWorldlevel(GlobalType.getWorldLevel());
        int remainTime = itemExpAddRate == 0 ? 0 : player.getHookInfo().getItemExpAddRateTime().get(itemExpAddRate);
        builder.setCurExpItemRamineTime(remainTime);
        MessageUtils.send_to_player(player.getId(), HookMessage.ResHookSetInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void addHookRate(HookMessage.ResHookSetInfo.Builder builder, int type, int rate) {
        HookMessage.hookExpAddRateInfo.Builder info = HookMessage.hookExpAddRateInfo.newBuilder();
        info.setType(type);
        info.setRate(rate);
        builder.addExpAddRateList(info);
    }

    /**
     * 请求开始打坐
     */
    @Override
    public void startSitDownHandler(Player player) {
        if (EntityState.Sitting.compare(player.getState())) {
            return;
        }
       ////骑乘状态不打坐
       //if (HorseRideStateEnum.Ride == player.getHorse().getRideState()) {
       //    return;
       //}

        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId());
        if (bean.getMeditation_whether() != 1) {
            return;
        }

        boolean canSitDown = checkCanSitDown(player);
        if (canSitDown) {
            player.addState(EntityState.Sitting);
            player.getHookInfo().setSittingStartTime((int) (TimeUtils.Time() / 1000));
            player.getHookInfo().setSittingBeforeExe(player.getTotalExp());
            player.getHookInfo().setSittingLevel(player.getLevel());
            player.getHookInfo().setSittingExe(0);
        }
        HookMessage.ResStartSitDown.Builder builder = HookMessage.ResStartSitDown.newBuilder();
        builder.setCanSitDown(canSitDown);
        builder.setRoleId(player.getId());
        MessageUtils.send_to_roundPlayer(player, HookMessage.ResStartSitDown.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        sendSyncExpAdd(player, 0);
    }

    private boolean checkCanSitDown(Player player) {
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId());
        if (mapBean == null) {
            return false;
        }
        return mapBean.getType() == MapDefine.WORLD_MAP;
    }

    /**
     * 结束打坐
     */
    @Override
    public void endSitDownHandler(Player player) {
        if (!EntityState.Sitting.compare(player.getState())) {
            return;
        }
        player.removeSate(EntityState.Sitting);
    }

    /**
     * 结束打坐的处理
     */
    @Override
    public void afterEndSitDown(Player player) {
        int sittingTime = (int) (TimeUtils.Time() / 1000) - player.getHookInfo().getSittingStartTime();
        if (sittingTime > 0) {
            Manager.countManager.addVariant(player, VariantType.Daily_Meditation_Time, sittingTime);
            Manager.countManager.addVariant(player, VariantType.AccuMeditation, sittingTime);
            Manager.controlManager.operate(player, FunctionVariable.MeditationTime, sittingTime);
        }

        HookMessage.ResEndSitDown.Builder builder = HookMessage.ResEndSitDown.newBuilder();
        builder.setRoleId(player.getId());
        builder.setSuccess(true);
        MessageUtils.send_to_roundPlayer(player, HookMessage.ResEndSitDown.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        long changeExp = player.getHookInfo().getSittingExe();

        int h = sittingTime / 3600;
        int m = sittingTime % 3600 / 60;
        int s = sittingTime % 3600 % 60;
        StringBuffer sb = new StringBuffer();
        sb.append((h < 10) ? "0" + h + ":" : "" + h + ":");
        sb.append((m < 10) ? "0" + m + ":" : "" + m + ":");
        sb.append((s < 10) ? "0" + s : "" + s);
        MessageUtils.notify_player(player, Notify.CHAT, MessageString.LeaderPreach_Tips, sb.toString(), changeExp);
        //结束时发送打坐经验BI
        int level = player.getHookInfo().getSittingLevel();
        BigInteger beforeExe = player.getHookInfo().getSittingBeforeExe();
        Manager.biManager.getScript().biResource(player, changeExp > 0 ? 1 : 0, ItemCoinType.EXP, BigInteger.valueOf(changeExp), beforeExe, player.getTotalExp(), level, player.getLevel(), ItemChangeReason.HookOnlineGet, IDConfigUtil.getLogId());
    }

    private void sendSyncExpAdd(Player player, long addExp) {
        int rate = Math.round((player.gainExpRate() - 1) * 100);
        HookMessage.ResSyncExpAdd.Builder builder = HookMessage.ResSyncExpAdd.newBuilder();
        builder.setAddExp(addExp);
        builder.setRate(rate);
        MessageUtils.send_to_player(player.getId(), HookMessage.ResSyncExpAdd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 离线，开始挂机
     */
    @Override
    public void enterOfflineHook(Player player) {
        //检查离线挂机
        if (checkCanEnterOfflineHook(player)) {
            player.getHookInfo().setOnHook(true);
        }
    }

    private boolean checkCanEnterOfflineHook(Player player) {
        if (GameServer.getInstance().IsFightServer()) {
            return false;
        }
        if (player.getHookInfo().isOnHook()) {
            return false;
        }
        //策划说只有两个，直接写死
        Item item = Manager.backpackManager.manager().getItemByModelId(player, Global.OnHook_automatic_addtime.get(0));
        Item item1 = Manager.backpackManager.manager().getItemByModelId(player, Global.OnHook_automatic_addtime.get(1));
        if (player.getSetting(SettingType.Auto_AddHookTime) && (item != null || item1 != null)) {
            return true;
        }
        return player.getHookInfo().getHookTime() > 0;
    }



    /**
     * 上线发送挂机找回时间
     * @param player
     */
    public void onlineSendHookFindTime(Player player){
        HookMessage.ResOfflineHookFindTime.Builder msg = HookMessage.ResOfflineHookFindTime.newBuilder();
        msg.setExp(player.getHookInfo().getHookFindExp());
        msg.setOfflineTime(player.getHookInfo().getTotalHookFindTime());
        MessageUtils.send_to_player(player.getId(), HookMessage.ResOfflineHookFindTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求找回
     * @param player
     * @param isfind
     */
    public void onReqOfflineHookFindTime(Player player,boolean isfind){
        if (!isfind){
            player.getHookInfo().setTotalHookFindTime(0);
            player.getHookInfo().setTotalHookFindTime(0);
            player.getHookInfo().setHookFindExp(0l);
            onlineSendHookFindTime(player);
            return;
        }
        float totalFindTime = player.getHookInfo().getTotalHookFindTime();
        long  totalFindExp  =  player.getHookInfo().getHookFindExp();
        if (totalFindExp <=0 || totalFindTime <=0){
            log.error("没有可找回时间 , 经验");
            return;
        }
        int  itemId = Global.EXP_GET_Multiple_BACK.get(0);
        int  sustainTime = Global.EXP_GET_Multiple_BACK.get(1) * 60;//一个经验丹能找回的时间
        int  price = Global.EXP_GET_Multiple_BACK.get(2);
        int needNum = 0;
        int needBuyNum = 0;
        int overtime =  (player.getHookInfo().getTotalHookFindTime() % sustainTime);
        //补差价
        int repairTime =overtime  > 0 ? sustainTime -overtime :0;
        needNum = totalFindTime > sustainTime ? (int)Math.ceil(totalFindTime / sustainTime):1;
        int hasNum =  Manager.backpackManager.manager().getItemNum(player,itemId );
        needBuyNum = needNum > hasNum ? (needNum - hasNum):0;
        int needCost = needNum - needBuyNum;
        long logid = IDConfigUtil.getLogId();
        if (needBuyNum > 0){
            int needMoney = needBuyNum * price;
            //扣钱
            if (  !Manager.currencyManager.manager().decBindGoldOrGold(player, needMoney,ItemChangeReason.HookFindTimeDec,logid)){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MyMoneyNotEnough);
                return;
            }
        }
        //扣道具
        if(needCost > 0){
            Manager.backpackManager.manager().onRemoveItem(player,itemId,needCost,ItemChangeReason.HookFindTimeDec,logid);
        }
        //添加经验
        Manager.currencyManager.manager().addEXP(player,totalFindExp,ItemChangeReason.HookOfflineGet,logid);
        if (repairTime  > 0){
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
            if ( itemBean.getEffect_num().size() < 1) {
                return;
            }
            ReadArray<Integer> readArray = itemBean.getEffect_num().get(0);
            if ( readArray.size() < 3) {
                return;
            }
            int rate = readArray.get(1);
            if (rate <= 0 ) {
                return;
            }
            ConcurrentHashMap<Integer, Integer> itemExpAddRateTime = player.getHookInfo().getItemExpAddRateTime();
            if (itemExpAddRateTime.containsKey(rate / 100)) {
                itemExpAddRateTime.put(rate / 100, itemExpAddRateTime.get(rate / 100) + repairTime);
            } else {
                itemExpAddRateTime.put(rate / 100, repairTime);
            }
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
            Manager.playerHookManager.deal().onReqHookSetInfoHandler(player);
        }
        player.getHookInfo().setTotalHookFindTime(0);
        player.getHookInfo().setTotalHookFindTime(0);
        player.getHookInfo().setHookFindExp(0l);
        onlineSendHookFindTime(player);
    }

    /**
     * 上线，挂机结束，返回挂机收益结果
     */
    @Override
    public void stopOfflineHook(Player player) {
        if (!player.getHookInfo().isOnHook()) {
            return;
        }
        int oldLevel = player.getLevel();
        player.getHookInfo().setOnHook(false);

        //离线时间，单位分钟
        int offLineTime = (int) ((TimeUtils.Time() - player.getOffLineTime()) / 1000 / 60);
        //离线时间不足十分钟
        if (offLineTime <= Global.OnHookMinAddTime) {
            return;
        }
        offLineTime = Math.min(Global.OnHookOnceMaxRewardTime, offLineTime);
        long actionId = IDConfigUtil.getLogId();

        //记录使用物品后的总时间，用于计算超过上限20小时，多余的部分要还给玩家
        int totalHookTime = player.getHookInfo().getHookTime();

        //自动补足离线挂机时间
        int[] itemUseNumArray = {0, 0};
        if (player.getSetting(SettingType.Auto_AddHookTime)) {
            //差值时间，物品数量，补充数量（舍余），使用数量
            int extraTime, itemNum, needNum, useNum;
            for (int i = 0; i < Global.OnHook_automatic_addtime.size(); i++) {
                if (i >= 2) {
                    break;
                }
                extraTime = offLineTime - player.getHookInfo().getHookTime() / 60;
                if (extraTime <= 0) {
                    break;
                }
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(Global.OnHook_automatic_addtime.get(i));
                if (itemBean.getEffect_num().get(0).get(0) != ItemEffectType.AddHookTime) {
                    continue;
                }
                itemNum = Manager.backpackManager.manager().getItemNum(player, Global.OnHook_automatic_addtime.get(i));
                needNum = (extraTime * 60) / itemBean.getEffect_num().get(0).get(1);
                if ((extraTime * 60) % itemBean.getEffect_num().get(0).get(1) != 0) {
                    needNum++;
                }
                useNum = Math.min(itemNum, needNum);
                if (useNum <= 0) {
                    continue;
                }
                int reallyUsed = 0, count = 0;
                while (useNum > 0 && count < 10) {
                    Item item = Manager.backpackManager.manager().getItemByModelId(player, Global.OnHook_automatic_addtime.get(i));
                    if (item == null) {
                        log.error("使用指定数量离线秘宝物品不存在：" + Global.OnHook_automatic_addtime.get(i));
                        continue;
                    }
                    int usedNum = Math.min(item.getNum(), useNum);
                    item.use(player, usedNum, 0, actionId);
                    reallyUsed += usedNum;
                    useNum -= usedNum;
                    count++;
                }
                itemUseNumArray[i] = reallyUsed;
                totalHookTime += reallyUsed * itemBean.getEffect_num().get(0).get(1);
            }
        }
        int needReturnHookTime = Math.max(0, totalHookTime - Global.OnHookMaxNum * 60);

        //最终离线时间收益时间，单位分钟
        int finalOffLineEarnTime = Math.min(offLineTime, player.getHookInfo().getHookTime() / 60);
        if (finalOffLineEarnTime <= 0) {
            return;
        }
        player.getHookInfo().setHookTime(player.getHookInfo().getHookTime() - finalOffLineEarnTime * 60);
        player.getHookInfo().setHookTime(player.getHookInfo().getHookTime() + needReturnHookTime);

        Cfg_On_hook_Bean bean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        //计算并增加经验
        long addExp = calculateExp(player, finalOffLineEarnTime * 60);
        Manager.currencyManager.manager().addEXP(player, addExp, ItemChangeReason.HookOfflineGet, actionId);

        //触发掉落次数
        int num = finalOffLineEarnTime / (Global.OnHook_drop_time_interval / 60);

        //是否包含任务掉落
        boolean hasTaskFlag = false;
        for (int i = 0; i < bean.getTask_id().size(); i++) {
            if (Manager.taskManager.deal().getTaskByModelId(player, bean.getTask_id().get(i)) != null) {
                hasTaskFlag = true;
                break;
            }
        }
        List<List<Integer>> items = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < bean.getNormaldrop().size(); j++) {
                List<List<Integer>> list = Manager.dropManager.deal().getItemDrops(player, bean.getNormaldrop().get(j));
                items.addAll(list);
            }
            if (hasTaskFlag) {
                for (int j = 0; j < bean.getTaskdrop().size(); j++) {
                    List<List<Integer>> list = Manager.dropManager.deal().getItemDrops(player, bean.getTaskdrop().get(j));
                    items.addAll(list);
                }
            }
        }
        List<List<Integer>> items1 = Manager.backpackManager.manager().getAfterMergeItemList(items);
        List<Item> itemList = Item.createItems(items1, 1);
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.HookOfflineGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL, MessageString.GetAwardNotEnoughSpaceContent, itemList, ItemChangeReason.HookOfflineGet);
        }

        //返回挂机结果
        sendOfflineHookResult(player, finalOffLineEarnTime * 60,
                player.getHookInfo().getHookTime(), oldLevel, player.getLevel(), addExp, itemList, itemUseNumArray);

        //记录日志
        HookLog hookLog = new HookLog();
        hookLog.setPlayer(player);
        hookLog.setLastLv(oldLevel);
        hookLog.setOnlineTime(finalOffLineEarnTime);
        hookLog.setCurrentLv(player.getLevel());
        hookLog.setAddExp(addExp);
        hookLog.setItems(JsonUtils.toJSONString(itemList));
        LogService.getInstance().execute(hookLog);
    }

    private void sendOfflineHookResult(Player player, int offlineTime, int hookRemainTime, int oldLevel,
                                       int newLevel, long addedExp, List<Item> items, int[] itemUseNum) {
        HookMessage.ResOfflineHookResult.Builder builder = HookMessage.ResOfflineHookResult.newBuilder();
        builder.setOfflineTime(offlineTime);
        builder.setHookRemainTime(hookRemainTime);
        builder.setOldLevel(oldLevel);
        builder.setNewLevel(newLevel);
        builder.setAddedExp(addedExp);
        builder.addAllItemInfoList(Manager.backpackManager.manager().buildItemInfo(items));
        builder.addUseNumList(itemUseNum[0]);
        builder.addUseNumList(itemUseNum[1]);
        MessageUtils.send_to_player(player.getId(), HookMessage.ResOfflineHookResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void sitDownEarning(Player player) {
        if (!EntityState.Sitting.compare(player.getState())) {
            return;
        }
        int now = (int) (TimeUtils.Time() / 1000);
        int startTime = player.getHookInfo().getSittingStartTime();
        int sittingTime = Math.max(now - startTime, 0);
        if (sittingTime < 10 || sittingTime % Global.OnHookClientTimes == 0) {
            return;
        }

        Cfg_On_hook_Bean bean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (bean == null) {
            return;
        }

        //计算经验
        long addExp = (long) (bean.getExp() * player.gainExpRate() * Global.OnHookClientTimes);
        Manager.currencyManager.manager().addEXP(player, addExp, ItemChangeReason.HookOnlineGet, 0);
        sendSyncExpAdd(player, addExp);

        player.getHookInfo().setSittingExe(addExp + player.getHookInfo().getSittingExe());

        //判定是否应掉落物品,打坐时长需要累计
        int historySittingTime = (int) Manager.countManager.getVariant(player, VariantType.AccuMeditation);
        int nowDropCount = (historySittingTime + sittingTime) / Global.OnHook_drop_time_interval;
        int lastDropCount = (historySittingTime + sittingTime - Global.OnHookClientTimes) / Global.OnHook_drop_time_interval;
        if (nowDropCount > lastDropCount) {
            List<List<Integer>> items = new ArrayList<>();
            //普通掉落
            for (int i = 0; i < bean.getNormaldrop().size(); i++) {
                List<List<Integer>> list = Manager.dropManager.deal().getItemDrops(player, bean.getNormaldrop().get(i));
                items.addAll(list);
            }
            //任务掉落
            for (int i = 0; i < bean.getTask_id().size(); i++) {
                if (Manager.taskManager.deal().getTaskByModelId(player, bean.getTask_id().get(i)) != null) {
                    for (int j = 0; j < bean.getTaskdrop().size(); j++) {
                        List<List<Integer>> list = Manager.dropManager.deal().getItemDrops(player, bean.getTaskdrop().get(j));
                        items.addAll(list);
                    }
                    break;
                }
            }
            List<Item> itemList = Item.createItems(items, 1);
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.HookOfflineGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, itemList, ItemChangeReason.HookOfflineGet);
            }
            if (itemList.size() > 0) {
                String itemName = Manager.backpackManager.manager().getName((itemList.get(0).getItemModelId()));
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.HOOK_DROP_TIPS, itemName);
            }
        }
    }

    /**
     * 定时发送地图经验
     */
    @Override
    public void sendSpecialMapPlayerExpUp(MapObject map) {

        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (bean == null || bean.getMap_exp() < 1) {
            return;
        }
        //判断配置条件是否满足
        for (Player player : map.getPlayers().values()) {
            if (!Manager.controlManager.deal().checkFuncProgress(player, bean.getMap_exp_condition())) {
                continue;
            }
            Cfg_On_hook_Bean hookBean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
            if (hookBean == null) {
                continue;
            }
            //计算经验
            long addExp = (long) (hookBean.getMap_exp().get(bean.getMap_exp() - 1) * player.gainExpRate() * Global.OnHookClientMapExpTimes);

            Manager.currencyManager.manager().addEXP(player, addExp, ItemChangeReason.HookMapGet, 0);
            sendSyncExpAdd(player, addExp);

            ZoneCache zone = map.getZone();
            if (zone instanceof ExpNoteData) {
                ExpNoteData peak = (ExpNoteData) zone;
                long last = peak.getExpNote().getOrDefault(player.getId(), 0L);
                peak.getExpNote().put(player.getId(), last + addExp);
            }
        }
    }
    /**
     * 计算离线挂机获得的经验
     *
     * @param player 玩家
     * @param time   离线挂机时间，单位秒
     * @return 基础经验加成后的经验
     */
    private long calculateExp(Player player, int time) {
        Cfg_On_hook_Bean bean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (bean == null) {
            return 0;
        }

        //离线要扣除经验倍率时间，并计算等效倍率
        float itemRate = calculateItemEquivalentRate(player, time);

        //实际倍率中替换 经验药倍率为等效倍率
        int rate = Math.round((player.gainExpRate() - player.expRateNotHaveAtt()) * 100);
        int realRate = rate - getCurrentItemRate(player) + Math.round(itemRate);
        if (player.getHookInfo().getHookFindTime() > 0 ){
           long addexp =   (long) ((bean.getExp() *  player.getHookInfo().getHookFindTime()) * 0.5);
           long addAllExp =   player.getHookInfo().getHookFindExp() +addexp;
           int totalTime =  player.getHookInfo().getTotalHookFindTime() + player.getHookInfo().getHookFindTime();
           player.getHookInfo().setHookFindExp(addAllExp);
           player.getHookInfo().setTotalHookFindTime(totalTime);
        }
        //等级经验 *（玩家自身(buff加成) + 其他经验加成）* 收益时间
        return (long) (bean.getExp() * (1 + realRate / 100.) * time);
    }

    /**
     * 计算药剂等效加成倍率，同时扣除经验倍率时间
     *
     * @param player       玩家
     * @param earningsTime 收益时间，单位秒
     * @return 等效加成倍率，百分率
     */
    private float calculateItemEquivalentRate(Player player, int earningsTime) {
        int tempTime = 0;
        int maxRate;
        int itemEffectTime;
        int tempEarnTime = earningsTime;
        ConcurrentHashMap<Integer, Integer> itemExpAddRateTime = player.getHookInfo().getItemExpAddRateTime();
        while (earningsTime > 0 && itemExpAddRateTime.size() > 0) {
            //优先使用最大生效倍率的药剂
            maxRate = itemExpAddRateTime.keySet().stream().reduce(0, Math::max);
            itemEffectTime = itemExpAddRateTime.get(maxRate);
            int useTime = Math.min(itemEffectTime, earningsTime);

            if (useTime == itemEffectTime) {
                //该药剂生效时间已消耗完
                itemExpAddRateTime.remove(maxRate);
            } else {
                //该药剂时间未消耗完
                itemExpAddRateTime.put(maxRate, itemEffectTime - useTime);
            }
            tempTime += useTime * maxRate;
            earningsTime -= useTime;
        }
        player.getHookInfo().setHookFindTime( earningsTime);
        return (float) (tempTime / tempEarnTime);
    }

    @Override
    public long getHookRate(long playerId) {
        Player player = Manager.playerManager.getPlayerCache(playerId);
        if (player == null) {
            return 0;
        }
        Cfg_On_hook_Bean bean = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (bean == null) {
            log.info("Cfg_On_hookBean 找不到，id" + player.getLevel());
            return 0;
        }
        return (long) (bean.getExp() * 60 * player.gainExpRate());
    }

    /**
     * 获取当前药剂加成，百分比
     */
    public int getCurrentItemRate(Player player) {
        return player.getHookInfo().getItemExpAddRateTime().keySet().stream().reduce(0, Math::max);
    }

    @Override
    public void decExpItemRateTime(Player player) {
        int lastRate = getCurrentItemRate(player);
        ConcurrentHashMap<Integer, Integer> itemExpAddRateTime = player.getHookInfo().getItemExpAddRateTime();
        if (lastRate == 0 || itemExpAddRateTime.get(lastRate) <= 0) {
            return;
        }
        int remainTime = itemExpAddRateTime.get(lastRate);
        if (remainTime > 1) {
            itemExpAddRateTime.put(lastRate, remainTime - 1);
            return;
        }
        itemExpAddRateTime.remove(lastRate);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
//        sendExpRateChange(player, PlayerHookManager.ItemRate, getCurrentItemRate(player));
    }

    //返回经验倍率变化消息
//    private void sendExpRateChange(Player player, int type, int rate) {
//        HookMessage.ResExpRateChange.Builder builder = HookMessage.ResExpRateChange.newBuilder();
//        HookMessage.hookExpAddRateInfo.Builder info = HookMessage.hookExpAddRateInfo.newBuilder();
//        info.setType(type);
//        info.setRate(rate);
//        builder.setInfo(info);
//        MessageUtils.send_to_player(player, HookMessage.ResExpRateChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
//    }

    @Override
    public void worldLvChange() {
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (!player.isOnline()) {
                continue;
            }
            checkPlayerRateChange(player, PlayerHookManager.WorldLvRate);
        }
    }

    @Override
    public void checkPlayerRateChange(Player player, int type) {
        if (player == null) {
            return;
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.MEDICINESATTRIBUTE);
    }
}
