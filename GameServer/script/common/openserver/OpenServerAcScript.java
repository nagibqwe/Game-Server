package common.openserver;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Xianmengzhengba_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.enums.ResourceType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.Boss;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.db.bean.RankPlayer;
import com.game.guild.structs.Guild;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.openserverac.manager.OpenServerAcManager;
import com.game.openserverac.scripts.IOpenServerAc;
import com.game.openserverac.structs.*;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.OpenServerAcMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OpenServerAcScript implements IOpenServerAc {
    private static final Logger log = LogManager.getLogger(OpenServerAcScript.class);

    //幸运翻牌中奖纪录存储条数
    private final int MAX_RECORD = 30;

    @Override
    public void initRevel(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServeCrazy)) {
            return;
        }

        initRevelInfo(player);

        OpenServerAcMessage.ResOpenSeverRevelList.Builder msg = OpenServerAcMessage.ResOpenSeverRevelList.newBuilder();
        msg.setOpenTime(TimeUtils.getOpenServerTime());

        for (OpenServerRevel revel : player.getOpenServerRevelMap().values()) {
            OpenServerAcMessage.OpenServerRevel.Builder msgRevel = OpenServerAcMessage.OpenServerRevel.newBuilder();
            Cfg_New_sever_rank_Bean rankBean = CfgManager.getCfg_New_sever_rank_Container().getValueByKey(revel.getId());
            if (rankBean == null) {
                continue;
            }
            if (TimeUtils.getOpenServerDay() == 1 && revel.getId() == OpenServerAcManager.Ac_Level_Talent && revel.getValue() == 0) {
                ConcurrentHashMap<Integer, Long> roleIds = RankListManager.getTempRankMap().get(RankType.LEVEL_RANK);
                initRevelMsg(player, msgRevel, roleIds, revel.getId());
            } else if (rankBean.getServerEndTime() == TimeUtils.getOpenServerDay() && revel.getValue() == 0 && (TimeUtils.getDayOfHour(TimeUtils.Time()) < Global.New_server_rewtime)) {
                if (revel.getId() == OpenServerAcManager.Ac_Recharge_Talent) {
                    intRevelRechargeMsg(player, msgRevel, revel.getId());
                } else {
                    ConcurrentHashMap<Integer, Long> roleIds = RankListManager.getTempRankMap().get(rankBean.getParm());
                    initRevelMsg(player, msgRevel, roleIds, revel.getId());
                }
            } else {
                msgRevel.setRank(revel.getRank());
                msgRevel.setCurValue(revel.getValue());
            }

            msgRevel.setId(revel.getId());
            Iterator iterator = revel.getPersonState().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();
                int id = entry.getKey();
                int state = entry.getValue();
                OpenServerAcMessage.PersonalRevel.Builder personalRevel = OpenServerAcMessage.PersonalRevel.newBuilder();
                Cfg_New_sever_rankrew_Bean bean = CfgManager.getCfg_New_sever_rankrew_Container().getValueByKey(id);
                if (state != OpenServerAcManager.State_2 && msgRevel.getCurValue() >= bean.getLimit()) {
                    personalRevel.setState(OpenServerAcManager.State_1);
                    revel.getPersonState().put(id, OpenServerAcManager.State_1);
                } else {
                    personalRevel.setState(state);
                }
                personalRevel.setId(id);
                msgRevel.addPList(personalRevel);
            }
            msg.addRevels(msgRevel);
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenSeverRevelList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }



    /**
     * 广播排名第一的玩家信息
     */
    public void onNoticeFirstRank(int rankType, ConcurrentHashMap<Integer, Long> roleIds){

        if (TimeUtils.getOpenServerDay() >= Global.New_server_growup_time) {
            return;
        }
        int id = 0;

        if (rankType == RankType.LEVEL_RANK) {
            id = OpenServerAcManager.Ac_Level_Talent;
        }
        if (rankType == RankType.PETASTAR_RANK) {
            id = OpenServerAcManager.Ac_Wash_Talent;
        }
        if (rankType == RankType.IMEQUIPSTAR_RANK) {
            id = OpenServerAcManager.Ac_EquipStar_Talent;
        }
        if (rankType == RankType.MAGIC_WEAPON_RANK) {
            id = OpenServerAcManager.Ac_Gem_Talent;
        }
        if (rankType == RankType.GEMLEVEL_RANK) {
            id = OpenServerAcManager.Ac_Fight_Talent;
        }
        if (rankType == RankType.HORSE_RANK) {
            id = OpenServerAcManager.Ac_Horse_Talent;
        }
        if (rankType == RankType.FIGHT_POWER_RANK) {
            id = OpenServerAcManager.Ac_Sprit_Talent;
        }


        if (id == 0) {
            return;
        }

        if (!isBalance(id)) {
            return;
        }
        if (roleIds.size() <= 0){
            return;
        }
        long roleId =  roleIds.get(1);
        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return;
        }
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (rankPlayer == null) {
            return;
        }


        Cfg_New_sever_rank_Bean synthesis_Bean = CfgManager.getCfg_New_sever_rank_Container().getValueByKey(id);
        if (synthesis_Bean == null){
            log.error("Cfg_New_sever_rank_Bean  is null :{} ",id);
            return;
        }

        if (synthesis_Bean.getNotice() != 0 || synthesis_Bean.getChatchannel() != null) {
            //TODO:公告
            MessageUtils.notify_allOnlinePlayer(synthesis_Bean.getNotice() , synthesis_Bean.getChatchannel(), MessageString.NEW_SEVER_RANK_NOTICE1,
                    player.getId()+"", player.getName(), ServerStr.getChatTableName(synthesis_Bean.getShowname()));
        }

    }


    @Override
    public void onOperateBalance(int rankType, ConcurrentHashMap<Integer, Long> roleIds) {
        log.info("onOperateBalance refresh:" + rankType + "rank size:" + roleIds.size());
        if (TimeUtils.getOpenServerDay() >= Global.New_server_growup_time) {
            return;
        }
        //特殊充值排行榜
        onOperateRechargeBalance(rankType);

        int id = 0;

        if (rankType == RankType.LEVEL_RANK) {
            id = OpenServerAcManager.Ac_Level_Talent;
        }
        if (rankType == RankType.PETASTAR_RANK) {
            id = OpenServerAcManager.Ac_Wash_Talent;
        }
        if (rankType == RankType.IMEQUIPSTAR_RANK) {
            id = OpenServerAcManager.Ac_EquipStar_Talent;
        }
        if (rankType == RankType.MAGIC_WEAPON_RANK) {
            id = OpenServerAcManager.Ac_Gem_Talent;
        }
        if (rankType == RankType.GEMLEVEL_RANK) {
            id = OpenServerAcManager.Ac_Fight_Talent;
        }
        if (rankType == RankType.HORSE_RANK) {
            id = OpenServerAcManager.Ac_Horse_Talent;
        }
        if (rankType == RankType.FIGHT_POWER_RANK) {
            id = OpenServerAcManager.Ac_Sprit_Talent;
        }


        if (id == 0) {
            return;
        }

        if (!isBalance(id)) {
            return;
        }

        List<Cfg_New_sever_rankrew_Bean> beans = getRewardCfgs(id, 1);
        if (beans.size() == 0) {
            return;
        }

        autoReward(id, roleIds, beans);
    }

    @Override
    public void onOperateRechargeGold(Player player, int num) {
//        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServeCrazy)) {
//            return;
//        }
        if (!isBalance(OpenServerAcManager.Ac_Recharge_Talent)) {
            return;
        }
        ServerParamUtil.serverRevelMap.put(player.getId(),
                ServerParamUtil.serverRevelMap.getOrDefault(player.getId(), 0) + num);
        ServerParamUtil.saveRevel();

    }

    @Override
    public void onReqPersonReward(Player player, int id) {
        Cfg_New_sever_rankrew_Bean bean = CfgManager.getCfg_New_sever_rankrew_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        if (bean.getSubType() != 0) {
            return;
        }

        OpenServerRevel openServerRevel = player.getOpenServerRevelMap().get(bean.getType());
        if (openServerRevel == null) {
            return;
        }


        if (openServerRevel.getPersonState().get(id) != OpenServerAcManager.State_1) {
            return;
        }

        openServerRevel.getPersonState().put(id, OpenServerAcManager.State_2);

        ReadIntegerArrayEs rewardMap = bean.getRew();
        List<List<Integer>> reward = new ArrayList<>();
        for (int i = 0; i < rewardMap.size(); i++) {
            if (rewardMap.get(i).get(2) == PlayerDefine.CAREER_All ||
                    rewardMap.get(i).get(2) == player.getCareer()) {
                List<Integer> l = new ArrayList<>();
                l.add(rewardMap.get(i).get(0));
                l.add(rewardMap.get(i).get(1));
                reward.add(l);
            }
        }
        List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByList(reward));

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerAcGet, id);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.OpenServerAcGet);
        }

        OpenServerAcMessage.ResOpenSeverRevelPersonReward.Builder msg = OpenServerAcMessage.ResOpenSeverRevelPersonReward.newBuilder();
        msg.setId(id);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenSeverRevelPersonReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //记录bi
        Cfg_New_sever_rank_Bean rankbean = CfgManager.getCfg_New_sever_rank_Container().getValueByKey(bean.getType());
        if (rankbean != null) {
            Manager.biManager.getInstance().getScript().biActivity(player, BIActiityTypeEnum.OPEN_SERVER_CRAZY, rankbean.getId(), rankbean.getShowname(), ItemChangeReason.OpenServerAcGet, 0, 0);
        }
    }

    @Override
    public void initGrowUp(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
            return;
        }
        initGrowupCfg(player);
        OpenServerAcMessage.ResGrowUpInfo.Builder msg = OpenServerAcMessage.ResGrowUpInfo.newBuilder();
        msg.setOpenTime(TimeUtils.getOpenServerTime());
        msg.setPoint(player.getOpenServerGrowUp().getPoint());
        msg.setHasGet(player.getOpenServerGrowUp().getHasGet());
        msg.setPrice(player.getOpenServerGrowUp().getPurPrice());
        Iterator<Map.Entry<Integer, GrowUpData>> iter = player.getOpenServerGrowUp().getNewGroupDatas().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, GrowUpData> i = iter.next();
            OpenServerAcMessage.GrowUp.Builder msgGrowUp = OpenServerAcMessage.GrowUp.newBuilder();
            //Cfg_New_sever_growup_Bean bean = CfgManager.getCfg_New_sever_growup_Container().getValueByKey(i.getKey());
            msgGrowUp.setId(i.getKey());
            msgGrowUp.setProgress(i.getValue().getProgress());
            msgGrowUp.setState(i.getValue().isHasGet());
            msg.addGrowups(msgGrowUp);
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqGrowUpPoint(Player player, int id) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
            return;
        }
        Cfg_New_sever_growup_Bean bean = CfgManager.getCfg_New_sever_growup_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        GrowUpData state = player.getOpenServerGrowUp().getNewGroupDatas().get(id);
        if (state == null) {
            return;
        }

        if (state.isHasGet() == true) {
            return;
        }

        if (state.getProgress() < bean.getCondition().get(bean.getCondition().size() - 1)) {
            log.error("条件未达成  id {}  getProgress {}", bean.getId(), state.getProgress());
            return;
        }

        int point = player.getOpenServerGrowUp().getPoint() + bean.getRate();

        player.getOpenServerGrowUp().setPoint(point);
        state.setHasGet(true);

        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(bean.getReward().get(0), bean.getReward().get(1), false);
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerGrowPointGet, actionId);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.OpenServerGrowPointGet);
        }

        Manager.biManager.getScript().biResource(player, 1, ResourceType.OpenServerGrowUpPoint.getId(), BigInteger.valueOf(bean.getRate()), BigInteger.valueOf(point - bean.getRate()), BigInteger.valueOf(point), player.getLevel(), player.getLevel(), ItemChangeReason.OpenServerGrowPointGet, actionId);

//        if (player.getOpenServerGrowUp().getPurPrice() > 0) {
//            Manager.currencyManager.manager().onAddGold(player, bean.getWorth(), ItemChangeReason.OpenServerGrowPoint, id);
//        }

        OpenServerAcMessage.ResGrowUpPoint.Builder msg = OpenServerAcMessage.ResGrowUpPoint.newBuilder();
        msg.setId(id);
        msg.setPoint(point);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpPoint.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //记录BI数据
        BIActiityTypeEnum e = null;
        if (bean.getSubId() == 1) {
            e = BIActiityTypeEnum.GrowthWay_one;
        } else if (bean.getSubId() == 2) {
            e = BIActiityTypeEnum.GrowthWay_two;
        } else if (bean.getSubId() == 3) {
            e = BIActiityTypeEnum.GrowthWay_three;
        } else if (bean.getSubId() == 4) {
            e = BIActiityTypeEnum.GrowthWay_four;
        } else {
            return;
        }
        Manager.biManager.getScript().biActivity(player, e, ItemChangeReason.OpenServerGrowPointGet);
    }

    @Override
    public void onReqGrowUpReward(Player player, int id) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
            return;
        }
        Cfg_New_sever_growuprew_Bean bean = CfgManager.getCfg_New_sever_growuprew_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        if (player.getOpenServerGrowUp().getPoint() < bean.getScroe()) {
            return;
        }

        int flag = player.getOpenServerGrowUp().getHasGet();
        if (hasGet(flag, id)) {
            return;
        }

        flag |= (1 << (id - 1));
        player.getOpenServerGrowUp().setHasGet(flag);

        List<Item> items = getItems(player, bean.getItem());
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerGrowPointRewardGet, id);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.OpenServerGrowPointRewardGet);
        }

        OpenServerAcMessage.ResGrowUpPointReward.Builder msg = OpenServerAcMessage.ResGrowUpPointReward.newBuilder();
        msg.setId(id);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpPointReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //记录BI数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.GrowthWay, ItemChangeReason.OpenServerGrowPointRewardGet, id);
    }

    private List<Item> getItems(Player player, ReadIntegerArrayEs itemBean) {
        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> l : itemBean.getValuees()) {
            if (l.get(3) != PlayerDefine.CAREER_All && l.get(3) != player.getCareer()) {
                continue;
            }
            items.addAll(Item.createItems(l.get(0), l.get(1), l.get(2) == 1));
        }
        return items;
    }

    @Override
    public void onReqGrowUpPur(Player player) {
//        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
//            return;
//        }
//
//        if (player.getOpenServerGrowUp().getPurPrice() > 0) {
//            return;
//        }
//
//        int needNum = 0;
//        Iterator<Map.Entry<Integer, Boolean>> iter = player.getOpenServerGrowUp().getGrowups().entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<Integer, Boolean> i = iter.next();
//            Cfg_New_sever_growup_Bean bean = CfgManager.getCfg_New_sever_growup_Container().getValueByKey(i.getKey());
//            if (bean == null) {
//                continue;
//            }
//
//            if (!i.getValue()) {
//                continue;
//            }
//            needNum += bean.getWorth();
//        }
//
//        needNum = Global.New_server_rew.get(1) - needNum;
//
//        if (needNum <= 0) {
//            needNum = 0;
//        }
//
//        if (needNum != 0) {
//            if (!Manager.currencyManager.manager().onDecItemCoin(player, needNum, ItemChangeReason.OpenServerGrowupPur, 0, ItemCoinType.GoldType)) {
//                return;
//            }
//        }
//        player.getOpenServerGrowUp().setPurPrice(needNum);
//
//        List<Item> items = Item.createItems(Global.New_server_rew.get(0), 1, true);
//        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
//            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerAc, Global.New_server_rew.get(0));
//        } else {
//            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items);
//        }
//        OpenServerAcMessage.ResGrowUpPur.Builder msg = OpenServerAcMessage.ResGrowUpPur.newBuilder();
//        msg.setPrice(needNum);
//        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpPur.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqGrowUpProgress(Player player, int type) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
            return;
        }
        OpenServerAcMessage.ResGrowUpList.Builder msg = OpenServerAcMessage.ResGrowUpList.newBuilder();
        //Iterator<Map.Entry<Integer, Boolean>> iter = player.getOpenServerGrowUp().getGrowups().entrySet().iterator();
        Iterator<Map.Entry<Integer, GrowUpData>> iter = player.getOpenServerGrowUp().getNewGroupDatas().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, GrowUpData> i = iter.next();
            GrowUpData growUpData = i.getValue();
            if (growUpData.isHasGet()) {
                continue;
            }

            Cfg_New_sever_growup_Bean bean = CfgManager.getCfg_New_sever_growup_Container().getValueByKey(i.getKey());
            if (bean == null) {
                continue;
            }

            if (bean.getCondition().get(0) != type) {
                continue;
            }
            int progress = getProgress(player, bean.getCondition());
            growUpData.setProgress(Math.max(growUpData.getProgress(), progress));

            OpenServerAcMessage.GrowUp.Builder msgGrowUp = OpenServerAcMessage.GrowUp.newBuilder();
            msgGrowUp.setId(i.getKey());
            msgGrowUp.setState(growUpData.isHasGet());
            msgGrowUp.setProgress(growUpData.getProgress());
            msg.addGrowups(msgGrowUp);
        }

        if (msg.getGrowupsCount() == 0) {
            return;
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqGrowUpNextProgress(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GrowthWay)) {
            return;
        }
        OpenServerAcMessage.ResGrowUpList.Builder msg = OpenServerAcMessage.ResGrowUpList.newBuilder();
        for (Cfg_New_sever_growup_Bean bean : getGrowupCfgs()) {
            if (player.getOpenServerGrowUp().getNewGroupDatas().get(bean.getId()) != null) {
                continue;
            }
            GrowUpData growUpData = new GrowUpData();
            growUpData.setId(bean.getId());
            growUpData.setProgress(getProgress(player, bean.getCondition()));
            growUpData.setHasGet(false);
            player.getOpenServerGrowUp().getNewGroupDatas().put(bean.getId(), growUpData);
            OpenServerAcMessage.GrowUp.Builder msgGrowUp = OpenServerAcMessage.GrowUp.newBuilder();
            msgGrowUp.setId(bean.getId());
            msgGrowUp.setState(false);
            msgGrowUp.setProgress(growUpData.getProgress());
            msg.addGrowups(msgGrowUp);
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGrowUpList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void initOpenServerSpec(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            return;
        }

        OpenServerAcMessage.ResOpenServerSpecRedDot.Builder msg = OpenServerAcMessage.ResOpenServerSpecRedDot.newBuilder();
        for (Cfg_New_sever_exchange_Bean bean : CfgManager.getCfg_New_sever_exchange_Container().getValuees()) {
            int hasCount = (int) Manager.countManager.getCount(player, BaseCountType.OpenServerDailyExchange, bean.getId());
            msg.addExchangeList(bean.getLimit_time() - hasCount);
        }

        initRed(player);

        boolean isRedDot = false;

        if (player.getOpenServerSpec().getRedList().size() >= 8 &&
                !player.getOpenServerSpec().isGetRed()) {
            isRedDot = true;
        }

        if (!isRedDot) {
            for (Cfg_New_sever_exchange_Bean bean : CfgManager.getCfg_New_sever_exchange_Container().getValuees()) {
                if (isExchange(player, bean)) {
                    isRedDot = true;
                    break;
                }
            }
        }

        if (!isRedDot) {
            for (Cfg_New_sever_active_Bean bean : CfgManager.getCfg_New_sever_active_Container().getValuees()) {
                if (getState(player, bean) == OpenServerAcManager.State_1) {
                    isRedDot = true;
                    break;
                }
            }
        }
        msg.setState(isRedDot);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecRedDot.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqOpenServerSpecProgress(Player player, int type) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            return;
        }

        if (TimeUtils.getOpenServerDay() >= 8) {
            return;
        }

        boolean isRedDot = false;

        for (Cfg_New_sever_active_Bean bean : CfgManager.getCfg_New_sever_active_Container().getValuees()) {
            if (bean.getCondition().get(0) != type) {
                continue;
            }

            if (getState(player, bean) == OpenServerAcManager.State_1) {
                isRedDot = true;
                break;
            }
        }

        if (!isRedDot) {
            return;
        }

        OpenServerAcMessage.ResOpenServerSpecRedDot.Builder msg = OpenServerAcMessage.ResOpenServerSpecRedDot.newBuilder();
        msg.setState(true);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecRedDot.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    @Override
    public void onReqOpenServerSpecAc(Player player) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            OpenServerAcMessage.ResOpenServerSpecAc.Builder msg = OpenServerAcMessage.ResOpenServerSpecAc.newBuilder();
            msg.setRedState(getRedState(player));
            msg.setOpenTime(TimeUtils.getOpenServerTime());
            msg.addAllRedList(player.getOpenServerSpec().getRedList());
            for (Cfg_New_sever_exchange_Bean bean : CfgManager.getCfg_New_sever_exchange_Container().getValuees()) {
                int hasCount = (int) Manager.countManager.getCount(player, BaseCountType.OpenServerDailyExchange, bean.getId());
                msg.addExchangeList(bean.getLimit_time() - hasCount);
            }

            for (Cfg_New_sever_active_Bean bean : CfgManager.getCfg_New_sever_active_Container().getValuees()) {
                OpenServerAcMessage.OpenServerSpec.Builder spec = OpenServerAcMessage.OpenServerSpec.newBuilder();
                spec.setId(bean.getId());
                spec.setProgress(getProgress(player, bean.getCondition()));
                spec.setState(getState(player, bean));
                if (bean.getLimit_time() > 0) {
                    spec.setRemain(bean.getLimit_time() - Manager.countManager.getServerCount(BaseCountType.OpenServerSpecGetCount, bean.getId()));
                } else {
                    spec.setRemain(0);
                }
                msg.addSpecList(spec);
            }
            MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecAc.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onReqOpenServerSpecReward(Player player, int id) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            Cfg_New_sever_active_Bean bean = CfgManager.getCfg_New_sever_active_Container().getValueByKey(id);
            if (bean == null) {
                return;
            }

            if (getState(player, bean) != OpenServerAcManager.State_1) {
                return;
            }

            player.getOpenServerSpec().getIsGetReward().put(id, true);
            OpenServerAcMessage.ResOpenServerSpecReward.Builder msg = OpenServerAcMessage.ResOpenServerSpecReward.newBuilder();
            if (bean.getLimit_time() > 0) {
                Manager.countManager.addServerCount(BaseCountType.OpenServerSpecGetCount, Count.RefreshType.CountType_Forever, bean.getId(), 1);
                msg.setRemain(bean.getLimit_time() - Manager.countManager.getServerCount(BaseCountType.OpenServerSpecGetCount, bean.getId()));
            } else {
                msg.setRemain(0);
            }

            //宗派特殊处理
            if (bean.getType() == 1) {
                int flag = Manager.countManager.getServerCount(BaseCountType.OpenServerGuildReward, player.getGuildId());
                flag |= (1 << (bean.getSort() - 1));
                Manager.countManager.setServerCount(BaseCountType.OpenServerSpecGetCount, Count.RefreshType.CountType_Forever, player.getGuildId(), flag);
            }

            List<Item> items = Item.createItems(bean.getItem());
            if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerSpecAc, id);
            } else {
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.OpenServerSpecAc);
            }

            msg.setId(id);
            MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onReqOpenServerSpecRed(Player player) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            return;
        }

        if (getRedState(player) != OpenServerAcManager.State_1) {
            return;
        }

        player.getOpenServerSpec().setGetRed(true);
        long actionId = IDConfigUtil.getLogId();
        Manager.currencyManager.manager().onAddItemCoin(player,
                ItemCoinType.BindGemCoin, player.getOpenServerSpec().getRedList().stream().reduce(Integer::sum).orElse(0),
                ItemChangeReason.OpenServerSpecAc, actionId);
        OpenServerAcMessage.ResOpenServerSpecRed.Builder msg = OpenServerAcMessage.ResOpenServerSpecRed.newBuilder();
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecRed.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqOpenServerSpeceExchange(Player player, int type) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            Cfg_New_sever_exchange_Bean bean = CfgManager.getCfg_New_sever_exchange_Container().getValueByKey(type);
            if (bean == null) {
                return;
            }

            if (isExchange(player, bean)) {
                OpenServerAcMessage.ResOpenServerSpecExchange.Builder msg = OpenServerAcMessage.ResOpenServerSpecExchange.newBuilder();
                if (bean.getLimit_time() > 0) {
                    Manager.countManager.addCount(player, BaseCountType.OpenServerDailyExchange, bean.getId(), Count.RefreshType.CountType_Day, 1);
                    msg.setRemain(bean.getLimit_time() - ((int) Manager.countManager.getCount(player, BaseCountType.OpenServerDailyExchange, bean.getId())));
                } else {
                    msg.setRemain(0);
                }

                List<Item> items = Item.createItems(bean.getReward());
                if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
                    Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.OpenServerSpecAcExchange, type);
                } else {
                    Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.OpenServerSpecAcExchange);
                }

                msg.setType(type);
                MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenServerSpecExchange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void onReqOpenServerSpecSaveRed(Player player, int num) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ServerActive)) {
            return;
        }
        if (TimeUtils.getOpenServerDay() >= 8) {
            return;
        }

        int curNum = player.getOpenServerSpec().getRedList().get(TimeUtils.getOpenServerDay() - 1);
        int addNum = Global.New_server_redbag.get(TimeUtils.getOpenServerDay() - 1).get(1) / 10000 * num + curNum;
        player.getOpenServerSpec().getRedList().remove(TimeUtils.getOpenServerDay() - 1);
        player.getOpenServerSpec().getRedList().add(addNum);
    }

    @Override
    public int getId() {
        return ScriptEnum.OpenServerAcBaseScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    private void initRevelInfo(Player player) {
        for (Cfg_New_sever_rank_Bean bean : CfgManager.getCfg_New_sever_rank_Container().getValuees()) {
            OpenServerRevel revel = player.getOpenServerRevelMap().get(bean.getId());
            if (revel == null) {
                revel = new OpenServerRevel();
                revel.setId(bean.getId());

                for (Cfg_New_sever_rankrew_Bean beanRew : getRewardCfgs(bean.getId(), 0)) {
                    revel.getPersonState().put(beanRew.getId(), OpenServerAcManager.State_0);
                }
                player.getOpenServerRevelMap().put(bean.getId(), revel);
            }
        }
    }

    private void initRevelMsg(Player player, OpenServerAcMessage.OpenServerRevel.Builder msgRevel, ConcurrentHashMap<Integer, Long> roleIds, int id) {
        List<Cfg_New_sever_rankrew_Bean> beans = getRewardCfgs(id, 1);
        if (beans.size() == 0) {
            return;
        }
        int tempRank = 0;
        boolean isCalRank = true;
        for (int i = 1; i <= roleIds.size(); i++) {
            if (roleIds.get(i) == null) {
                continue;
            }

            Player otherPlayer = Manager.playerManager.getPlayer(roleIds.get(i));
            if (otherPlayer == null) {
                continue;
            }


            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(otherPlayer.getId());
            if (rankPlayer == null) {
                continue;
            }

            int myRank = 0;
            if (isCalRank) {
                int resTeampRank = getRank(rankPlayer, id, tempRank, beans);
                if (resTeampRank == 0) {
                    isCalRank = false;
                } else {
                    tempRank = resTeampRank;
                    myRank = tempRank;
                }
            }

            if (player.getId() == otherPlayer.getId()) {
                msgRevel.setCurValue(getValue(rankPlayer, id));
                msgRevel.setRank(myRank);
                return;
            }
        }
        msgRevel.setCurValue(0);
        msgRevel.setRank(0);
    }

    private void intRevelRechargeMsg(Player player, OpenServerAcMessage.OpenServerRevel.Builder msgRevel, int id) {
        if (ServerParamUtil.serverRevelMap.get(player.getId()) == null) {
            msgRevel.setCurValue(0);
            msgRevel.setRank(0);
            return;
        }

        List<Map.Entry<Long, Integer>> list = new ArrayList<>(ServerParamUtil.serverRevelMap.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        ConcurrentHashMap<Integer, Long> roleIds = new ConcurrentHashMap<>();
        int rank = 0;
        for (Map.Entry<Long, Integer> i : list) {
            rank += 1;
            roleIds.put(rank, i.getKey());
        }

        initRevelMsg(player, msgRevel, roleIds, id);
    }

    private boolean isBalance(int id) {
        Cfg_New_sever_rank_Bean bean = CfgManager.getCfg_New_sever_rank_Container().getValueByKey(id);
        if (bean == null) {
            return false;
        }

        if (TimeUtils.getOpenServerDay() != bean.getServerEndTime()) {
            return false;
        }
        return true;
    }

    private void autoReward(int id, ConcurrentHashMap<Integer, Long> roleIds, List<Cfg_New_sever_rankrew_Bean> beans) {
        int tempRank = 0;
        boolean isCalRank = true;
        for (int i = 1; i <= roleIds.size(); i++) {
            if (roleIds.get(i) == null) {
                continue;
            }

            Player player = Manager.playerManager.getPlayer(roleIds.get(i));
            if (player == null) {
                continue;
            }

            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
            if (rankPlayer == null) {
                continue;
            }

            int myRank = 0;
            if (isCalRank) {
                int resTeampRank = getRank(rankPlayer, id, tempRank, beans);
                if (resTeampRank == 0) {
                    isCalRank = false;
                } else {
                    tempRank = resTeampRank;
                    myRank = tempRank;
                }
            }

            if (player.getOpenServerRevelMap().get(id) != null) {
                //最后一天发所有玩家奖励补发
                player.getOpenServerRevelMap().get(id).setRank(myRank);
                player.getOpenServerRevelMap().get(id).setValue(getValue(rankPlayer, id));

                if (id == OpenServerAcManager.Ac_Fight_Talent) {
                    for (OpenServerRevel openServerRevel : player.getOpenServerRevelMap().values()) {
                        Iterator iterator = openServerRevel.getPersonState().entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();
                            int value = entry.getValue();
                            int personId = entry.getKey();
                            Cfg_New_sever_rankrew_Bean bean = CfgManager.getCfg_New_sever_rankrew_Container().getValueByKey(personId);
                            if (value != OpenServerAcManager.State_2 && openServerRevel.getValue() >= bean.getLimit()) {
                                openServerRevel.getPersonState().put(personId, OpenServerAcManager.State_2);
                                ReadIntegerArrayEs rewardMap = bean.getRew();
                                List<List<Integer>> reward = new ArrayList<>();
                                for (int j = 0; j < rewardMap.size(); j++) {
                                    if (rewardMap.get(j).get(2) == PlayerDefine.CAREER_All ||
                                            rewardMap.get(j).get(2) == player.getCareer()) {
                                        List<Integer> l = new ArrayList<>();
                                        l.add(rewardMap.get(j).get(0));
                                        l.add(rewardMap.get(j).get(1));
                                        reward.add(l);
                                    }
                                }
                                List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByList(reward));
                                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.SeverCrazyPersonalRewardTitle, MessageString.SeverCrazyPersonalRewardContent, items, ItemChangeReason.SeverCrazyPersonalReward);
                            }
                        }
                    }
                }
            }

            if (myRank != 0) {
                ReadIntegerArrayEs rewardMap = getCfg(myRank, beans).getRew();
                List<List<Integer>> reward = new ArrayList<>();
                for (int j = 0; j < rewardMap.size(); j++) {
                    if (rewardMap.get(j).get(2) == PlayerDefine.CAREER_All ||
                            rewardMap.get(j).get(2) == player.getCareer()) {
                        List<Integer> l = new ArrayList<>();
                        l.add(rewardMap.get(j).get(0));
                        l.add(rewardMap.get(j).get(1));
                        reward.add(l);
                    }
                }
                List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByList(reward));
                Cfg_New_sever_rank_Bean bean = CfgManager.getCfg_New_sever_rank_Container().getValueByKey(id);
                String showname = bean == null ? "" : bean.getShowname();
                showname = ServerStr.getChatTableName(showname);
                String content = MessageString.SeverCrazyRankRewardContent + "@_@" + showname + "@_@" + myRank;
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.SeverCrazyRankRewardTitle, content, items, ItemChangeReason.SeverCrazyRankRewardGain);
            }
        }
    }

    private int getRank(RankPlayer rankPlayer, int id, int tempRank, List<Cfg_New_sever_rankrew_Bean> beans) {
        Cfg_New_sever_rankrew_Bean bean = getCfg(tempRank + 1, beans);
        if (bean == null) {
            return 0;
        }

        int value = getValue(rankPlayer, id);

        if (value >= bean.getLimit()) {
            return tempRank + 1;
        } else {
            return getRank(rankPlayer, id, bean.getMaxRank(), beans);
        }
    }

    private int getValue(RankPlayer rankPlayer, int id) {
        int value = 0;
        if (id == OpenServerAcManager.Ac_Level_Talent) {
            value = rankPlayer.getLevel();
        }
        if (id == OpenServerAcManager.Ac_EquipStar_Talent) {
            value = rankPlayer.getImmEquipFightPower();
        }
        if (id == OpenServerAcManager.Ac_Wash_Talent) {
            value = rankPlayer.getPetFightPower();
        }
        //TODO暂时取消
        if (id == OpenServerAcManager.Ac_Recharge_Talent) {
            value = ServerParamUtil.serverRevelMap.getOrDefault(rankPlayer.getRoleId(), 0);
        }
        if (id == OpenServerAcManager.Ac_Gem_Talent) {
            value = rankPlayer.getMagicWeaponDamage();
        }
        if (id == OpenServerAcManager.Ac_Fight_Talent) {
            value = rankPlayer.getGemLv();
        }
        if (id == OpenServerAcManager.Ac_Sprit_Talent) {
            value = (int) rankPlayer.getFightPower();
        }
        if (id == OpenServerAcManager.Ac_Horse_Talent) {
            value = rankPlayer.getHorseFightPoint();
        }


        return value;
    }

    private List<Cfg_New_sever_rankrew_Bean> getRewardCfgs(int id, int subType) {
        List<Cfg_New_sever_rankrew_Bean> list = new ArrayList<>();
        for (Cfg_New_sever_rankrew_Bean bean : CfgManager.getCfg_New_sever_rankrew_Container().getValuees()) {
            if (bean.getType() == id && bean.getSubType() == subType) {
                list.add(bean);
            }
        }
        return list;
    }


    private Cfg_New_sever_rankrew_Bean getCfg(int rank, List<Cfg_New_sever_rankrew_Bean> list) {
        for (Cfg_New_sever_rankrew_Bean bean : list) {
            if (rank >= bean.getMinRank() && rank <= bean.getMaxRank()) {
                return bean;
            }
        }

        return null;
    }


    private void onOperateRechargeBalance(int rankType) {
        if (RankType.LEVEL_RANK != rankType) {
            return;
        }

        int id = OpenServerAcManager.Ac_Recharge_Talent;
        if (!isBalance(id)) {
            return;
        }

        List<Map.Entry<Long, Integer>> list = new ArrayList<>(ServerParamUtil.serverRevelMap.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        List<Cfg_New_sever_rankrew_Bean> beans = getRewardCfgs(id, 1);
        if (beans.size() == 0) {
            return;
        }

        ConcurrentHashMap<Integer, Long> roleIds = new ConcurrentHashMap<>();
        int rank = 0;
        for (Map.Entry<Long, Integer> i : list) {
            rank += 1;
            roleIds.put(rank, i.getKey());
        }

        autoReward(id, roleIds, beans);
    }


    private void initGrowupCfg(Player player) {
        for (Cfg_New_sever_growup_Bean bean : CfgManager.getCfg_New_sever_growup_Container().getValuees()) {
            if (bean.getDay() > TimeUtils.getOpenServerDay()) {
                continue;
            }

            if (player.getOpenServerGrowUp().getNewGroupDatas().get(bean.getId()) != null) {
                continue;
            }

            GrowUpData growUpData = new GrowUpData();
            growUpData.setHasGet(false);
            growUpData.setProgress(getProgress(player, bean.getCondition()));
            growUpData.setId(bean.getId());
            player.getOpenServerGrowUp().getNewGroupDatas().put(bean.getId(), growUpData);
        }
    }

    private List<Cfg_New_sever_growup_Bean> getGrowupCfgs() {
        List<Cfg_New_sever_growup_Bean> cfgs = new ArrayList<>();
        for (Cfg_New_sever_growup_Bean bean : CfgManager.getCfg_New_sever_growup_Container().getValuees()) {
            if (bean.getDay() == TimeUtils.getOpenServerDay()) {
                cfgs.add(bean);
            }
        }
        return cfgs;
    }

    private boolean hasGet(int hasget, int id) {
        return (hasget & (1 << (id - 1))) != 0;
    }

    private int getProgress(Player player, ReadArray<Integer> param) {
        return Manager.controlManager.deal().getFuncProgress(player, param);
    }

    private int getState(Player player, Cfg_New_sever_active_Bean bean) {
        Boolean state = player.getOpenServerSpec().getIsGetReward().get(bean.getId());
        if (state != null && state) {
            return OpenServerAcManager.State_2;
        }

        if (bean.getLimit_time() > 0) {
            int havaGetCount = Manager.countManager.getServerCount(BaseCountType.OpenServerSpecGetCount, bean.getId());
            if (havaGetCount >= bean.getLimit_time()) {
                return OpenServerAcManager.State_0;
            }
        }

        //宗派活动特殊处理
        if (bean.getType() == 1) {
            if (player.getGuildId() == 0) {
                return OpenServerAcManager.State_0;
            }

            Guild g = Manager.guildsManager.getGuildById(player.getGuildId());
            if (g == null) {
                return OpenServerAcManager.State_0;
            }

//            if (g.getOrgMember().getMasterId() != player.getId()) {
//                return OpenServerAcManager.State_0;
//            }

            int havaGetCount = Manager.countManager.getServerCount(BaseCountType.OpenServerGuildReward, player.getGuildId());
            if (hasGet(havaGetCount, bean.getSort())) {
                return OpenServerAcManager.State_2;
            }
        }

        if (Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())) {
            return OpenServerAcManager.State_1;
        } else {
            return OpenServerAcManager.State_0;
        }
    }

    private int getRedState(Player player) {
        if (player.getOpenServerSpec().isGetRed()) {
            return OpenServerAcManager.State_2;
        }

        if (TimeUtils.getOpenServerDay() >= 8) {
            return OpenServerAcManager.State_1;
        }

        return OpenServerAcManager.State_0;
    }

    private boolean isExchange(Player player, Cfg_New_sever_exchange_Bean bean) {
        if (bean.getLimit_time() != 0) {
            int hasCount = (int) Manager.countManager.getCount(player, BaseCountType.OpenServerDailyExchange, bean.getId());
            if (hasCount >= bean.getLimit_time()) {
                return false;
            }
        }

        if (!Manager.backpackManager.manager().canDeleteItemNum(player, bean.getItem(), 1)) {
            return false;
        }

        return true;
    }

    private void initRed(Player player) {
        if (TimeUtils.getOpenServerDay() > player.getOpenServerSpec().getRedList().size()) {
            int day = TimeUtils.getOpenServerDay() - player.getOpenServerSpec().getRedList().size();
            for (int i = 0; i < day; i++) {
                player.getOpenServerSpec().getRedList().add(0);
            }
        } else if (TimeUtils.getOpenServerDay() < player.getOpenServerSpec().getRedList().size()) {
            int day = player.getOpenServerSpec().getRedList().size() - TimeUtils.getOpenServerDay();
            for (int i = 0; i < day; i++) {
                player.getOpenServerSpec().getRedList().remove(player.getOpenServerSpec().getRedList().size() - 1);
            }
        }
    }

    @Override
    public void onReqFreeDailyReward(Player player) {
        if (TimeUtils.getOpenServerDay() > Global.Function_Notice_Open_Time) {
            return;
        }
        if (player.isOsDailyRewardGet()) {
            return;
        }

        List<Item> createItems = Item.createItems(Global.Function_Notice_Daily_Reward);
        if (!Manager.backpackManager.manager().addItems(player, createItems, ItemChangeReason.OpenServerAcNoticeReward, IDConfigUtil.getLogId())) {
            if (!Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, createItems, ItemChangeReason.OpenServerAcNoticeReward)) {
                return;
            }
        }

        player.setOsDailyRewardGet(true);
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Function_Notice_Reward_Title);
        sendFreeDailyReward(player);
    }

    @Override
    public void loginFreeDailyReward(Player player) {
        if (TimeUtils.getOpenServerDay() > Global.Function_Notice_Open_Time) {
            return;
        }

        if (!TimeUtils.isSameDay(TimeUtils.Time(), player.getLastOpsFuncRewardTime())) {
            player.setOsDailyRewardGet(false);
            player.setLastOpsFuncRewardTime(TimeUtils.Time());
        }

        sendFreeDailyReward(player);
    }

    @Override
    public void resetFreeDailyReward(Player player) {
        if (TimeUtils.getOpenServerDay() > Global.Function_Notice_Open_Time) {
            return;
        }

        player.setOsDailyRewardGet(false);
        player.setLastOpsFuncRewardTime(TimeUtils.Time());

        if (player.isOnline()) {
            sendFreeDailyReward(player);
        }
    }

    private void sendFreeDailyReward(Player player) {
        OpenServerAcMessage.ResFreeDailyReward.Builder msg = OpenServerAcMessage.ResFreeDailyReward.newBuilder();
        msg.setHasGet(player.isOsDailyRewardGet());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResFreeDailyReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqFirstKillPanel(Player player, boolean online) {
        if (TimeUtils.getOpenServerDay() >= 8) {
            return;
        }
        OpenServerAcMessage.ResOpenFirstKillPanel.Builder builder = OpenServerAcMessage.ResOpenFirstKillPanel.newBuilder();
        int state;
        ConcurrentHashMap<Integer, Boss> bossMap;
        boolean redPoint = false;
        for (Cfg_Boss_FirstBlood_Bean bean : CfgManager.getCfg_Boss_FirstBlood_Container().getValuees()) {
            OpenServerAcMessage.killInfo.Builder info = OpenServerAcMessage.killInfo.newBuilder();
            info.setCfgId(bean.getID());
            info.setRoleId(0);

            long time = 0L;
            String killInfo = ServerParamUtil.serverFirstKillList.getOrDefault(bean.getMonster_id(), "");
            if (!Strings.isEmpty(killInfo)) {
                String[] killInfo1 = killInfo.split("_");
                time = Long.valueOf(killInfo1[0]);
                String[] names = killInfo1[1].split(",");
                info.addAllKillers(Arrays.asList(names));
                info.setLevel(Integer.valueOf(killInfo1[2]));
                info.setCareer(Integer.valueOf(killInfo1[3]));


                //判断参数个数
                if (killInfo1.length > 4) {
                    long playerId = Long.parseLong(killInfo1[4]);
                    info.setRoleId(playerId);
                    PlayerWorldInfo playerWorldInfo = PlayerManager.getInstance().getPlayerWorldInfo(playerId);
                    if (playerWorldInfo != null) {
                        info.setHead(MapUtils.getHead(playerWorldInfo));
                    }
                }

                if (player.getFirstKillRedPacket().contains(bean.getID())) {
                    info.setRedpacketState(2);
                } else {
                    info.setRedpacketState(1);
                    redPoint = true;
                }
            } else {
                info.setRedpacketState(0);
            }
            state = player.getFirstKillData().getOrDefault(bean.getMonster_id(), 0);
            if (state == 1) {
                redPoint = true;
            }
            if (state == 0 && bean.getBoss_type() != 3) {
                if (bean.getBoss_type() == 1) {
                    bossMap = BossManager.getWorldBossMap();
                } else if (bean.getBoss_type() == 2) {
                    bossMap = Manager.bossManager.getSuitBossMap();
                } else if (bean.getBoss_type() == 4) {
                    bossMap = Manager.bossManager.getBossHome();
                } else {
                    bossMap = BossManager.getGemBossMap();
                }
                Boss boss = bossMap.get(bean.getMonster_id());
                if (boss == null) {
                    log.error("指定类型的boss" + bean.getBoss_type() + "，实际不存在该bossId:" + bean.getMonster_id());
                    continue;
                }
                info.setReliveTime(boss.getNextTime());
            }
            info.setKillTime(time);
            info.setState(state);
//            Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(bean.getMonster_id());
//            log.info(String.format("%s_%s_%s, type:%s, killer:%s, killTime:%s, state:%s, redstate:%s, retime:%s",
//                bean.getID(), bean.getMonster_id(), monsterBean.getName(), bean.getBoss_type(), info.getKiller(), info.getKillTime(), info.getState(), info.getRedpacketState(), info.getReliveTime()));
            builder.addBossInfo(info);
        }
        //if (online) {
        //    if (redPoint) {
        //       OpenServerAcMessage.ResFirstKillRedPoint.Builder b = OpenServerAcMessage.ResFirstKillRedPoint.newBuilder();
        //       b.setCfgId(0);
        //       b.setState(1);
        //       b.setRedpacketState(1);
        //       MessageUtils.send_to_player(player, OpenServerAcMessage.ResFirstKillRedPoint.MsgID.eMsgID_VALUE, b.build().toByteArray());
        //    }
        //    return;
        //}
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResOpenFirstKillPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        CrossServerMessage.G2PReqFirstKillBossRefreshTime.Builder msg = CrossServerMessage.G2PReqFirstKillBossRefreshTime.newBuilder();
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(CrossServerMessage.G2PReqFirstKillBossRefreshTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqHongBaoReward(Player player, int id) {
        if (TimeUtils.getOpenServerDay() >= 8) {
            return;
        }
        Cfg_Boss_FirstBlood_Bean bean = CfgManager.getCfg_Boss_FirstBlood_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }
        String killInfo = ServerParamUtil.serverFirstKillList.getOrDefault(bean.getMonster_id(), "");
        if (Strings.isEmpty(killInfo)) {
            return;
        }
        if (player.getFirstKillRedPacket().contains(id)) {
            return;
        }
        player.getFirstKillRedPacket().add(id);
        for (int i = 0; i < bean.getFirst_blood_cash().size(); i++) {
            ReadArray<Integer> array = bean.getFirst_blood_cash().get(i);
            Manager.currencyManager.manager().onAddItemCoin(player, array.get(0), array.get(1), ItemChangeReason.FirstKillRedPacket, IDConfigUtil.getLogId());
        }
        OpenServerAcMessage.ResHongBaoReward.Builder builder = OpenServerAcMessage.ResHongBaoReward.newBuilder();
        builder.setId(id);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResHongBaoReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //记录BI数据
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.KILL_BOSS, ItemChangeReason.FirstKillRedPacket);
    }

    @Override
    public void onReqGetKillReward(Player player, int id) {
        Cfg_Boss_FirstBlood_Bean bean = CfgManager.getCfg_Boss_FirstBlood_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }
        int state = player.getFirstKillData().getOrDefault(bean.getMonster_id(), 0);
        if (state != 1) {
            return;
        }
        player.getFirstKillData().put(bean.getMonster_id(), 2);

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < bean.getPersonal_reward().size(); i++) {
            ReadArray<Integer> array = bean.getPersonal_reward().get(i);
            if (array.get(0) != player.getCareer() && array.get(0) != PlayerDefine.CAREER_All) {
                continue;
            }
            itemList.add(Item.createItem(array.get(1), array.get(2), array.get(3) == 1));
        }

        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.PersonFirstKillRewardGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.PersonFirstKillRewardGet);
        }
        OpenServerAcMessage.ResGetKillReward.Builder builder = OpenServerAcMessage.ResGetKillReward.newBuilder();
        builder.setId(id);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetKillReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Manager.biManager.getInstance().getScript().biActivity(player, BIActiityTypeEnum.KILL_BOSS, ItemChangeReason.PersonFirstKillRewardGet);
    }

    @Override
    public void onKillMonster(int modelId, List<Long> roleIds, List<String> names) {
        if (TimeUtils.getOpenServerDay() >= 8) {
            return;
        }

        Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(modelId);
        if (monsterBean == null) {
            return;
        }
        Cfg_Boss_FirstBlood_Bean bossBean = null;
        for (Cfg_Boss_FirstBlood_Bean bean : CfgManager.getCfg_Boss_FirstBlood_Container().getValuees()) {
            if (bean.getMonster_id() == modelId) {
                bossBean = bean;
                break;
            }
        }
        if (bossBean == null) {
            return;
        }


        boolean serverFirstKill = !ServerParamUtil.serverFirstKillList.containsKey(modelId);
        for (int i = 0; i < roleIds.size(); i++) {
            Player p = Manager.playerManager.getPlayerCache(roleIds.get(i));
            if (p == null) {
                return;
            }

            Integer state = p.getFirstKillData().getOrDefault(modelId, 0);
            if (state == 0) {
                p.getFirstKillData().put(modelId, 1);
                Manager.controlManager.operate(p, FunctionVariable.BossFirstBloodreward, 0);

                OpenServerAcMessage.ResFirstKillRedPoint.Builder builder = OpenServerAcMessage.ResFirstKillRedPoint.newBuilder();
                builder.setCfgId(bossBean.getID());//客户端喊改成boss配置表ID
                builder.setState(1);
                builder.setRedpacketState(1);
                MessageUtils.send_to_player(p, OpenServerAcMessage.ResFirstKillRedPoint.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }

            if (serverFirstKill && i <= 2) {
                List<Item> itemList = new ArrayList<>();
                for (int j = 0; j < bossBean.getFirst_blood_reward().size(); j++) {
                    ReadArray<Integer> array = bossBean.getFirst_blood_reward().get(j);
                    if (array.get(0) != p.getCareer() && array.get(0) != PlayerDefine.CAREER_All) {
                        continue;
                    }
                    itemList.add(Item.createItem(array.get(1), array.get(2), array.get(3) == 1));
                }
                String context = MessageString.FirstKillBoss_KillReward_Content + "@_@" + ServerStr.getChatTableName(monsterBean.getName());
                Manager.mailManager.sendMailToPlayer(p.getId(), 1, MessageString.System,
                        MessageString.FirstKillBoss_KillReward_Ttile, context, itemList, ItemChangeReason.FirstKillBossKillRewardGain);
            }
        }

        if (serverFirstKill) {
            Player player = Manager.playerManager.getPlayerCache(roleIds.get(0));
            StringBuilder nameStr = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                nameStr.append(names.get(i));
                if (i == names.size() - 1 || i == 2) {
                    break;
                }
                nameStr.append(",");
            }
            //修改存储
            ServerParamUtil.serverFirstKillList.put(modelId, TimeUtils.Time() + "_" + nameStr.toString() + "_" + player.getLevel() + "_" + player.getCareer() + "_" + player.getId());
            ServerParamUtil.saveServerFirstKillData();
            log.info(player.getName() + "[" + nameStr + "]首杀了boss：" + modelId);


            OpenServerAcMessage.ResFirstKillAdvice.Builder builder = OpenServerAcMessage.ResFirstKillAdvice.newBuilder();
            builder.setCfgId(bossBean.getID());
            builder.setKiller(player.getName());
            builder.setLevel(player.getLevel());
            builder.setCareer(player.getCareer());
            builder.setFight(player.getFightPoint());
            builder.setRoleId(player.getId());

//            int headId = 0;
//            int headFrameId = 0;
//            if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//                 headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//            }
//            if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//                headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//            }
//            builder.setHeadId(headId);
//            builder.setHeadFrameId(headFrameId);

            builder.setHead(MapUtils.getHead(player));

            MessageUtils.send_to_all_player(OpenServerAcMessage.ResFirstKillAdvice.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    @Override
    public void checkTimeOver(Player player) {
        if (TimeUtils.getOpenServerDay() < 8) {
            if (!player.getFirstKillRedPacket().contains(0)) {
                player.getFirstKillRedPacket().add(0);
            }
            return;
        }
        List<Item> itemList = new ArrayList<>();
        for (Cfg_Boss_FirstBlood_Bean bean : CfgManager.getCfg_Boss_FirstBlood_Container().getValuees()) {

            String killInfo = ServerParamUtil.serverFirstKillList.getOrDefault(bean.getMonster_id(), "");
            if (!Strings.isEmpty(killInfo) && player.getFirstKillRedPacket().contains(0) && !player.getFirstKillRedPacket().contains(bean.getID())) {
                player.getFirstKillRedPacket().add(bean.getID());
                for (int i = 0; i < bean.getFirst_blood_cash().size(); i++) {
                    ReadArray<Integer> array = bean.getFirst_blood_cash().get(i);
                    itemList.add(Item.createItem(array.get(0), array.get(1), false));
                }
            }

            int state = player.getFirstKillData().getOrDefault(bean.getMonster_id(), 0);
            if (state == 1) {
                player.getFirstKillData().put(bean.getMonster_id(), 2);
                for (int i = 0; i < bean.getPersonal_reward().size(); i++) {
                    ReadArray<Integer> array = bean.getPersonal_reward().get(i);
                    if (array.get(0) != player.getCareer() && array.get(0) != PlayerDefine.CAREER_All) {
                        continue;
                    }
                    itemList.add(Item.createItem(array.get(1), array.get(2), false));
                }
            }
        }
        if (itemList.isEmpty()) {
            return;
        }
        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                MessageString.FirstKillBoss_UnReward_Ttile, MessageString.FirstKillBoss_UnReward_Content, itemList, ItemChangeReason.FirstKillBossKillRewardGain);
    }

    @Override
    public void checkCompleteActTask(Player player) {
        boolean needSync = false;
        out:
        for (int i = 0; i < Global.New_Sever_Activity_Time.size(); i++) {
            ReadArray<Integer> array = Global.New_Sever_Activity_Time.get(i);
            if (array.size() < 3) {
                continue;
            }
            int type = array.get(0);
            //int openServerDay = TimeUtils.getOpenServerDay();
            //if (openServerDay > array.get(2)) {
            //    continue;
            //}
            NewServerActInfo info = player.getActInfo().get(type);
            if (info == null) {
                info = new NewServerActInfo(type);
                player.getActInfo().put(type, info);
            }
            for (Cfg_New_active_advantage_Bean bean : CfgManager.getCfg_New_active_advantage_Container().getValuees()) {
                if (bean.getActiveType() != type || bean.getType() == 2) {
                    continue;
                }
                boolean isGet = info.getOverList().contains(bean.getId());
                if (isGet) {
                    continue;
                }
                if (info.getCompletList().contains(bean.getId())) {
                    continue;
                }
                if (Manager.controlManager.deal().checkFuncProgress(player, bean.getValue())) {
                    info.getCompletList().add(bean.getId());
                    needSync = true;
                    break out;
                }
            }
        }
        if (needSync) {
            sendNewServerActInfo(player);
        }
    }

    @Override
    public void checkActEnd(long lastCheckTime, long now) {
        //long openServerBeginTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
        //for (int i = 0; i < Global.New_Sever_Activity_Time.size(); i++) {
        //    ReadArray<Integer> array = Global.New_Sever_Activity_Time.get(i);
        //    long endTime = openServerBeginTime + array.get(2) * GlobalType.MILLIS_PER_DAY;
        //    if (lastCheckTime < endTime && now >= endTime) {
        //        for (Player player : Manager.playerManager.getOnLines()) {
        //            sendNewServerActInfo(player);
        //        }
        //    }
        //}
    }

    @Override
    public void sendNewServerActInfo(Player player) {
        OpenServerAcMessage.ResNewServerActPanel.Builder builder = OpenServerAcMessage.ResNewServerActPanel.newBuilder();
        long openServerBeginTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
        for (int i = 0; i < Global.New_Sever_Activity_Time.size(); i++) {
            ReadArray<Integer> array = Global.New_Sever_Activity_Time.get(i);
            if (array.size() < 3) {
                continue;
            }
            long now = TimeUtils.Time();
            int type = array.get(0);
            long startTime = openServerBeginTime + (array.get(1) - 1) * GlobalType.MILLIS_PER_DAY;
            //long endTime = openServerBeginTime + array.get(2) * GlobalType.MILLIS_PER_DAY;

            //if (now > endTime + 30L * GlobalType.MILLIS_PER_DAY) {
            //    continue;
            //}

            OpenServerAcMessage.actInfo.Builder actInfo = OpenServerAcMessage.actInfo.newBuilder();
            actInfo.setType(type);
            actInfo.setStartTime((int) (startTime / 1000));
            actInfo.setEndTime(0);

            NewServerActInfo info = player.getActInfo().get(type);
            if (info == null) {
                info = new NewServerActInfo(type);
                player.getActInfo().put(type, info);
            }
            int progress = 0;
            List<Item> notGetCanReward = new ArrayList<>();
            for (Cfg_New_active_advantage_Bean bean : CfgManager.getCfg_New_active_advantage_Container().getValuees()) {
                if (bean.getActiveType() != type) {
                    continue;
                }
                OpenServerAcMessage.cfgItem.Builder cfgItem = OpenServerAcMessage.cfgItem.newBuilder();
                cfgItem.setId(bean.getId());
                cfgItem.setPro(0);
                boolean isGet = info.getOverList().contains(bean.getId());
                if (bean.getType() != 2) {
                    boolean isComplete = Manager.controlManager.deal().checkFuncProgress(player, bean.getValue());
                    if (isComplete) progress++;
                }
                if (!isGet) {
                    if (bean.getType() == 2) {
                        cfgItem.setPro(progress);
                    } else {
                        cfgItem.setPro(Manager.controlManager.deal().getFuncProgress(player, bean.getValue()));
                    }

                    //检查过期未领取的奖励
                    //if (now > endTime) {
                    //boolean isFinish = checkIsFinish(player, bean);
                    //if (isFinish) {
                    //    isGet = true;
                    //    info.getOverList().add(bean.getId());
                    //    notGetCanReward.addAll(Item.createItems(player.getCareer(), bean.getReward(), 1));
                    //}
                    // }
                }
                cfgItem.setIsGet(isGet);
                actInfo.addItems(cfgItem);
            }
            builder.addInfos(actInfo);

            if (!notGetCanReward.isEmpty()) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.New_Active_Mail_Title, MessageString.New_Active_Mail, notGetCanReward, ItemChangeReason.NewActive);
            }
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResNewServerActPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqGetActReward(Player player, int type, int id) {
        Cfg_New_active_advantage_Bean bean = CfgManager.getCfg_New_active_advantage_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }
        if (bean.getActiveType() != type) {
            return;
        }
        NewServerActInfo actInfo = player.getActInfo().get(type);
        if (actInfo == null) {
            actInfo = new NewServerActInfo(type);
            player.getActInfo().put(type, actInfo);
        }
        if (actInfo.getOverList().contains(id)) {
            return;
        }
        boolean isFinish = checkIsFinish(player, bean);
        if (isFinish) {
            actInfo.getOverList().add(id);
            List<Item> itemList = Item.createItems(player.getCareer(), bean.getReward(), 1);
            if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.NewServerAdvantage, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemList, ItemChangeReason.NewServerAdvantage);
            }
            //记录BI数据
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.XFHD_XFHD, ItemChangeReason.NewServerAdvantage);
        }

        OpenServerAcMessage.ResGetActReward.Builder builder = OpenServerAcMessage.ResGetActReward.newBuilder();
        builder.setType(type);
        builder.setId(id);
        builder.setIsGet(isFinish);
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetActReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        for (ReadArray<Integer> readArray : bean.getReward().getValuees()) {
            if (readArray.get(3) == player.getCareer() || readArray.get(3) == 9) {
                Manager.controlManager.operate(player, FunctionVariable.GetEquipsId, readArray.get(0));
            }
        }
    }

    @Override
    public void playerOnline(Player player) {
        int openDay = TimeUtils.getOpenServerDay();
        if (openDay >= 16) {
            return;
        }
        //返利宝箱
        rebateBoxPlayerOnline(player, openDay);

        if (openDay >= 8) {
            return;
        }
        //推送幸运翻牌数据
        OpenSeverSpec openSeverSpec = player.getOpenServerSpec();
        OpenServerAcMessage.ResLuckyCardInfo.Builder builder = OpenServerAcMessage.ResLuckyCardInfo.newBuilder();
        //幸运值
        builder.setLucky(openSeverSpec.getLuckyValue());
        //任务状态
        for (Cfg_New_sever_luckcard_Bean bean : CfgManager.getCfg_New_sever_luckcard_Container().getValuees()) {
            OpenServerAcMessage.luckyCardTask.Builder taskPb = OpenServerAcMessage.luckyCardTask.newBuilder();
            taskPb.setId(bean.getId());
            //如果配置了 需要多个条件同时满足，则分母肯定是1
            if (bean.getCondition().getValuees().length > 1) {
                taskPb.setFenmu(1);
            } else {
                ReadArray readArray = bean.getCondition().get(0);
                taskPb.setFenmu((Integer) readArray.get(readArray.size() - 1));
            }
            taskPb.setFenzi(openSeverSpec.getLuckyTask().getOrDefault(bean.getId(), 0));
            //未完成
            if (taskPb.getFenzi() < taskPb.getFenmu()) {
                taskPb.setState(1);
            } else {
                //0=完成未领取 2=已领取
                taskPb.setState((openSeverSpec.getLuckyTaskSta() & 1 << bean.getId()) > 0 ? 2 : 0);
            }
            builder.addTasks(taskPb);
        }
        //已经抽到过的奖励列表
        for (Map.Entry<Integer, Integer> entry : openSeverSpec.getLuckyRewardHis().entrySet()) {
            OpenServerAcMessage.luckyCardRewardGot.Builder gotPb = OpenServerAcMessage.luckyCardRewardGot.newBuilder();
            //key是格子id
            gotPb.setCellId(entry.getKey());
            //value是奖励下标
            ReadArray<Integer> arr = Global.New_Sever_Luck_reward.get(entry.getValue());
            //item_num_bind_occ_probability
            gotPb.setItemId(arr.get(0));
            gotPb.setNum(arr.get(1));
            gotPb.setBind(arr.get(2) == 1);
            gotPb.setOcc(arr.get(3));
            builder.addGotRewards(gotPb);
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResLuckyCardInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        //仙盟争霸
        guildBattlePlayerOnline(player);
    }

    /**
     * 返利宝箱玩家在线
     * @param player
     * @param openDay
     */
    private void rebateBoxPlayerOnline(Player player, int openDay) {
        //返利宝箱--发送提醒邮件
        sendBoxRemind(player, openDay);
        //返利宝箱--补发返利宝箱奖励
        getRebateBoxReward(player, openDay);
        //返利宝箱--推送返利宝箱信息
        sendRebateBoxInfo(player);
    }

    @Override
    public void getLuckyCardWish(Player player, int taskId) {
        Cfg_New_sever_luckcard_Bean task = null;
        for (Cfg_New_sever_luckcard_Bean bean : CfgManager.getCfg_New_sever_luckcard_Container().getValuees()) {
            if (bean.getId() == taskId) {
                task = bean;
                break;
            }
        }
        if (task == null) {
            log.error("任务找不到" + taskId);
            return;
        }
        OpenSeverSpec openSeverSpec = player.getOpenServerSpec();
        if ((openSeverSpec.getLuckyTaskSta() & 1 << taskId) > 0) {
            log.error("奖励已领取");
            return;
        }
        Integer fenzi = openSeverSpec.getLuckyTask().getOrDefault(taskId, 0);
        ReadArray<Integer> arr = task.getCondition().get(0);
        //最后一个是分母
        if (fenzi < arr.get(arr.size() - 1)) {
            log.error("任务还未完成");
            return;
        }
        //保存已领取状态
        openSeverSpec.setLuckyTaskSta(openSeverSpec.getLuckyTaskSta() | 1 << taskId);
        //增加幸运值
        openSeverSpec.addLuckyValue(task.getReward());
        //返回前端
        OpenServerAcMessage.ResGetLuckyTaskReawrd.Builder res = OpenServerAcMessage.ResGetLuckyTaskReawrd.newBuilder();
        OpenServerAcMessage.luckyCardTask.Builder taskPb = OpenServerAcMessage.luckyCardTask.newBuilder();
        taskPb.setId(taskId);
        taskPb.setFenzi(fenzi);
        taskPb.setFenmu(arr.get(arr.size() - 1));
        taskPb.setState(2);
        res.setTask(taskPb);
        res.setLucky(openSeverSpec.getLuckyValue());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetLuckyTaskReawrd.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void luckyOnce(Player player, int cellId) {
        OpenSeverSpec spec = player.getOpenServerSpec();
        //界面上只有9张牌。玩家翻牌次数不可能超过9次
        if (spec.getLuckyRewardHis().size() >= 9) {
            log.error("翻牌次数超过九次" + player.getId());
            return;
        }
        if (spec.getLuckyValue() < Global.New_Sever_Luck_Limit) {
            log.error("幸运值不足");
            return;
        }
        if (spec.getLuckyRewardHis().containsKey(cellId)) {
            log.error("这个位置已经翻过了");
            return;
        }
        //已经获得的奖励索引
        HashSet<Integer> gotIndex = new HashSet<>(spec.getLuckyRewardHis().values());
        ArrayList<Integer> indexList = new ArrayList<>();//符合条件的下标列表
        ArrayList<Integer> weightList = new ArrayList<>();//符合条件的权重列表
        for (int index = 0; index < Global.New_Sever_Luck_reward.getValuees().length; index++) {
            //已经获得的不再获得
            if (gotIndex.contains(index)) {
                continue;
            }
            //item_num_bind_occ_probability
            ReadArray<Integer> arr = Global.New_Sever_Luck_reward.get(index);
            //不同职业的不能获得
            int occ = arr.get(3);
            if (occ != 9 && occ != player.getCareer()) {
                continue;
            }
            indexList.add(index);
            int value = arr.get(4);
            weightList.add(value);
            if (weightList.size() > 1) {
                weightList.set(weightList.size() - 1, value + weightList.get(weightList.size() - 2));
            }
        }
        ReadArray<Integer> reaward = null;
        int ran = new Random().nextInt(weightList.get(weightList.size() - 1));
        for (int i = 0; i < weightList.size(); i++) {
            if (ran < weightList.get(i)) {
                int index = indexList.get(i);
                reaward = Global.New_Sever_Luck_reward.get(index);
                //增加记录
                spec.getLuckyRewardHis().put(cellId, index);
                break;
            }
        }
        if (reaward == null) {
            log.error("奖励找不到");
            return;
        }
        //扣幸运值
        spec.addLuckyValue(-Global.New_Sever_Luck_Limit);
        //生成中奖纪录
        LuckyCardRewardLog rewardLog = new LuckyCardRewardLog();
        rewardLog.setId(reaward.get(0));
        rewardLog.setNum(reaward.get(1));
        rewardLog.setTime(TimeUtils.Time());
        rewardLog.setPlayerName(player.getName());
        //加到全服记录里
        List<LuckyCardRewardLog> records = ServerParamUtil.luckyCardRecords;
        records.add(rewardLog);
        while (records.size() >= MAX_RECORD) {
            records.remove(0);
        }

        //奖励进包裹
        //增加道具
        List<Item> itemList = new ArrayList<>();
        itemList.addAll(Item.createItems(reaward.get(0), reaward.get(1), reaward.get(2) == 0 ? false : true));
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.LuckyCardGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, itemList, ItemChangeReason.LuckyCardGet);
        }
        //返回结果
        OpenServerAcMessage.ResLuckyOnce.Builder res = OpenServerAcMessage.ResLuckyOnce.newBuilder();
        OpenServerAcMessage.luckyCardRewardGot.Builder got = OpenServerAcMessage.luckyCardRewardGot.newBuilder();
        got.setCellId(cellId);
        got.setItemId(reaward.get(0));
        got.setNum(reaward.get(1));
        got.setBind(reaward.get(2) == 1);
        got.setOcc(reaward.get(3));
        res.setReward(got);
        res.setLucky(spec.getLuckyValue());
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResLuckyOnce.MsgID.eMsgID_VALUE, res.build().toByteArray());
        //保存全服抽奖记录
        ServerParamUtil.saveLuckyCardRecord();

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.LUCKY_CARD, ItemChangeReason.LuckyCardGet);
    }

    @Override
    public void onAddluckyTaskFenzi(Player player) {
        OpenSeverSpec openSeverSpec = player.getOpenServerSpec();
        HashMap<Integer, Integer> task = openSeverSpec.getLuckyTask();

        for (Cfg_New_sever_luckcard_Bean bean : CfgManager.getCfg_New_sever_luckcard_Container().getValuees()) {
            int taskId = bean.getId();
            ReadArray<Integer> arr = bean.getCondition().get(0);
            Integer fenzi = task.getOrDefault(taskId, 0);
            //分子已经≥分母了，不再++
            if (fenzi >= arr.get(arr.size() - 1)) {
                continue;
            }
            int progress = Manager.controlManager.deal().getFuncProgress(player, arr);
            if (progress == 0) {
                continue;
            }
            fenzi += progress;
            task.put(bean.getId(), fenzi);

            //推送任务变更
            OpenServerAcMessage.ResGetLuckyTaskReawrd.Builder res = OpenServerAcMessage.ResGetLuckyTaskReawrd.newBuilder();
            OpenServerAcMessage.luckyCardTask.Builder taskPb = OpenServerAcMessage.luckyCardTask.newBuilder();
            taskPb.setId(bean.getId());
            taskPb.setFenzi(fenzi);
            taskPb.setFenmu(arr.get(arr.size() - 1));
            if ((openSeverSpec.getLuckyTaskSta() & 1 << bean.getId()) > 0) {
                taskPb.setState(2);
            } else if (taskPb.getFenzi() < taskPb.getFenmu()) {
                taskPb.setState(1);
            } else {
                taskPb.setState(0);
            }
            res.setLucky(openSeverSpec.getLuckyValue());
            res.setTask(taskPb);
            MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetLuckyTaskReawrd.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }

    @Override
    public void getLuckyHistroy(Player player) {
        OpenServerAcMessage.ResGetLuckyLog.Builder res = OpenServerAcMessage.ResGetLuckyLog.newBuilder();
        ServerParamUtil.luckyCardRecords.forEach(log -> res.addRecords(log.bytesWriteToClient()));
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetLuckyLog.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void onCostGold(Player player, int cost) {
        int day = TimeUtils.getOpenServerDay();
        if (day >= 8) {
            return;
        }
        player.getOpenServerSpec().addLuckyValue(cost / Global.New_Sever_Luck_Value.get(0).get(0) * Global.New_Sever_Luck_Value.get(0).get(1));
        //返利宝箱
        int[] box = player.getRebateBoxData().getBoxs();
        box[day - 1] += cost;
        playerOnline(player);
    }

    private boolean checkIsFinish(Player player, Cfg_New_active_advantage_Bean bean) {

        NewServerActInfo actInfo = player.getActInfo().get(bean.getActiveType());
        if (actInfo == null) {
            actInfo = new NewServerActInfo(bean.getActiveType());
            player.getActInfo().put(bean.getActiveType(), actInfo);
        }
        boolean isFinish = true;
        if (bean.getType() == 2) {
            for (Cfg_New_active_advantage_Bean tempBean : CfgManager.getCfg_New_active_advantage_Container().getValuees()) {
                if (tempBean.getActiveType() != bean.getActiveType()) {
                    continue;
                }
                if (tempBean.getType() == 2) {
                    continue;
                }
                if (!actInfo.getCompletList().contains(tempBean.getId())) {
                    isFinish = false;
                    break;
                }
            }
        } else {
            if (!actInfo.getCompletList().contains(bean.getId())) {
                isFinish = false;
            }
        }
        return isFinish;
    }

    @Override
    public void getRebateBox(Player player, int day) {
        int openDay = TimeUtils.getOpenServerDay();
        //开服大于七天才能领取
        if(openDay > 7){
            if(openDay - 7 >= day){
                Set<Integer> days = player.getRebateBoxData().getBoxDays();
                boolean success = false;
                synchronized (days){
                    //没领取就可以领取
                    if(!days.contains(day)){
                        days.add(day);
                        int[] boxs = player.getRebateBoxData().getBoxs();
                        int total = boxs[day - 1];
                        if(total > 0){
                            int get = total * Global.RebateBox_Percent / 100;
                            if(get > 0){
                                Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GemCoin, get, ItemChangeReason.RebateBoxGet, IDConfigUtil.getLogId());
                            }
                            success = true;
                        }
                    }
                }
                if(success){
                    sendRebateBoxInfo(player);
                }
            }
        }
    }

    /**
     * 发送返利宝箱数据
     * @param player
     */
    private void sendRebateBoxInfo(Player player) {
        //补发未领取的奖励
        OpenServerAcMessage.ResRebateBoxList.Builder res = OpenServerAcMessage.ResRebateBoxList.newBuilder();
        int boxs[] = player.getRebateBoxData().getBoxs();
        for(int box : boxs){
            res.addNum(box);
        }
        for(int day : player.getRebateBoxData().getBoxDays()){
            res.addDay(day);
        }
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResRebateBoxList.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 获取返利宝箱奖励
     * @param player
     */
    private void getRebateBoxReward(Player player, int openDay) {
        int day = openDay - 8;
        for(; day > 0; day--){
            if(!player.getRebateBoxData().getBoxDays().contains(day)){
                //奖励道具
                int total = player.getRebateBoxData().getBoxs()[day - 1];
                if(total > 0){
                    //发送邮件
                    player.getRebateBoxData().getBoxDays().add(day);
                    int get = Global.RebateBox_Percent * total / 100;
                    if(get > 0){
                        List<Item> items = Item.createItems(ItemCoinType.GemCoin, get, false);
                        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System,
                                MessageString.REBATEBOX_MAIL_TITLE, MessageString.REBATEBOX_MAIL_CONTENT2, items, ItemChangeReason.RebateBoxGet);
                    }
                }
            }
        }
    }

    /**
     * 返利宝箱--发送提醒邮件
     * @param player
     * @param openDay
     */
    private void sendBoxRemind(Player player, int openDay) {
        int[] boxRemind = player.getRebateBoxData().getBoxRemind();
        int remindDay = openDay - 1;
        if(remindDay > 0 && remindDay <= boxRemind.length){
            for(; remindDay > 0; remindDay--){
                int total = player.getRebateBoxData().getBoxs()[remindDay - 1];
                if(total > 0){
                    int get = total * Global.RebateBox_Percent / 100;
                    if(get > 0){
                        if(boxRemind[remindDay - 1] == 0){
                            boxRemind[remindDay - 1] = 1;

                            //发邮件
                            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System,
                                    MessageString.REBATEBOX_MAIL_TITLE, MessageString.REBATEBOX_MAIL_CONTENT1 + "@_@" + get);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void zeroClockDeal(Player player) {
        try{
            int openDay = TimeUtils.getOpenServerDay();
            if (openDay >= 16) {
                return;
            }
            rebateBoxPlayerOnline(player, openDay);
        }catch (Exception e){
            log.error("", e);
        }
    }

    /**
     * 仙盟争霸玩家上线
     * @param player
     */
    private void guildBattlePlayerOnline(Player player) {
        Map<Integer, GuildBattleData> datas = player.getGuildBattleData();

        OpenServerAcMessage.ResXMZhengBaInfo.Builder res = OpenServerAcMessage.ResXMZhengBaInfo.newBuilder();
        for(Cfg_Xianmengzhengba_Bean bean : Cfg_Xianmengzhengba_Container.GetInstance().getValuees()){
            OpenServerAcMessage.XMZhengBa.Builder o = OpenServerAcMessage.XMZhengBa.newBuilder();
            o.setId(bean.getId());
            GuildBattleData data = datas.get(bean.getId());
            if(data == null){
                o.setProgress(0);
                o.setIsComplete(false);
            }else{
                o.setProgress(data.getProgress());
                o.setIsComplete(data.isHasGet());
            }
            res.addXmzbList(o);
        }
        res.setEndTime((int)((TimeUtils.Time() + 7 * GlobalType.MILLIS_PER_DAY) / 1000));
        MessageUtils.send_to_player(player, OpenServerAcMessage.ResXMZhengBaInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void onRefreshGuildBattle(Player player, int changeNum, int... types) {
        try{
            boolean update = false;
            for(Cfg_Xianmengzhengba_Bean bean : Cfg_Xianmengzhengba_Container.GetInstance().getValuees()){
                ReadIntegerArray value = bean.getValue();
                int type = types[0];
                if(type == value.get(0)){
                    boolean ok = true;
                    if(types.length > 1){
                        if(types.length != value.size() - 1){
                            ok = false;
                            continue;
                        }
                        for(int i = 1; i<types.length;i++){
                            //单独处理的类型
                            if(type == 234 || type == 235){//激活X阶X色以上斗心||合成一个X阶以上的圣装
                                if(types[i] < value.get(i)){
                                    ok = false;
                                    break;
                                }
                            }else{
                                if(types[i] != value.get(i)){
                                    ok = false;
                                    break;
                                }
                            }
                        }

                    }

                    if(!ok){
                        continue;
                    }

                    //数据更新
                    GuildBattleData data = player.getGuildBattleData().get(bean.getId());
                    if(data == null){
                        data = new GuildBattleData();
                        data.setId(bean.getId());
                        data.setHasGet(false);
                        player.getGuildBattleData().put(bean.getId(), data);
                    }
                    if(data.isHasGet()){
                        continue;
                    }
                    int old = data.getProgress();
                    int num = Manager.controlManager.deal().getFuncProgress(player, value);
                    if(num > 0){
                        data.setProgress(num);
                    }else{
                        data.setProgress(old + changeNum);
                    }
                    //如果变为可领取，推送消息
                    int need = value.get(value.size() - 1);
                    if(data.getProgress() > need){
                        data.setProgress(need);
                    }
                    if(data.getProgress() > old){
                        update = true;
                    }
                }
            }
            if(update){
                guildBattlePlayerOnline(player);
            }
        }catch (Exception e){
            log.error("onRefreshGuildBattle error " + "player:" + player.getId(), e);
        }
    }

    @Override
    public void onReqXmzbReward(Player player, int id) {
        GuildBattleData data = player.getGuildBattleData().get(id);
        if(data == null){
            return;
        }
        Cfg_Xianmengzhengba_Bean bean = Cfg_Xianmengzhengba_Container.GetInstance().getValueByKey(id);
        if(bean == null){
            return;
        }
        ReadIntegerArray value = bean.getValue();
        int need = value.get(value.size() - 1);
        //是否已完成且还未领取
        if(data.getProgress() >= need && !data.isHasGet()){
            data.setHasGet(true);
            //领取
            List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
            Manager.backpackManager.manager().addOrSendItems(player, items, ItemChangeReason.XMZBGet, IDConfigUtil.getLogId());
            OpenServerAcMessage.ResGetXMZBReward.Builder res = OpenServerAcMessage.ResGetXMZBReward.newBuilder();

            OpenServerAcMessage.XMZhengBa.Builder o = OpenServerAcMessage.XMZhengBa.newBuilder();
            o.setId(bean.getId());
            o.setProgress(data.getProgress());
            o.setIsComplete(data.isHasGet());
            res.setXmzb(o);
            MessageUtils.send_to_player(player, OpenServerAcMessage.ResGetXMZBReward.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }
}
