package common.jjc;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_JJCAeward_Bean;
import com.data.bean.Cfg_JJCRank_Bean;
import com.data.bean.Cfg_Jjcrobot_Bean;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.JJCBean;
import com.game.db.bean.RankPlayer;
import com.game.db.dao.JJCDao;
import com.game.jjc.manager.JJCManager;
import com.game.jjc.script.IJJCHandler;
import com.game.jjc.structs.JJC;
import com.game.jjc.structs.JJCReport;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.ranklist.manager.RankListManager;
import com.game.robot.ai.RobotAi;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import com.game.vip.structs.VipPower;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.JJCMessage;
import game.message.JJCMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class JJCManagerScript implements IJJCHandler {

    public final Logger logger = LogManager.getLogger(JJCManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.JJCManagerScriptBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnReqAddChance(Player player, JJCMessage.ReqAddChance mess) {
        //增加VIP宝珠状态检查
        if(!player.getVipPearl().canFree()){
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_Vip_Power_No_Ues_Notice);
            return;
        }
        int canBuyNum = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_23);
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.JJC.getValue(), 0);
        if (buyCount >= canBuyNum) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.JJCAddCountFail);
            return;
        }
        int gold = Manager.vipManager.power().getVipAddNumPrice(buyCount + 1, VipPower.POWER_23);
        if (!Manager.currencyManager.manager().decBindGoldOrGold(player, gold, ItemChangeReason.JJCBuyCountGetDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GoldNotEnough);
            return;
        }

        player.getDailyActiveData().getDailyBuyCount().put(DailyActiveDefine.JJC.getValue(), buyCount + 1);

        //资源找回记录购买册数
        Manager.retrieveResManager.getScript().addVipBuyCount(player,DailyActiveDefine.JJC.getValue(),1);

        MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.C_SHOP_TIPS_BUYSUXESSSSSS);
        updateChance(player);
        sendJJCBuyTime(player);
    }

    private void updateChance(Player player) {
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.JJC.getValue());
        ResUpdateChance.Builder msg = ResUpdateChance.newBuilder();
        msg.setCount(remainCount);
        msg.setCd(0);
        msg.setRank(jjc.getScore());
        MessageUtils.send_to_player(player, JJCMessage.ResUpdateChance.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendJJCBuyTime(Player player) {
        JJCMessage.ResBuyJJCTimes.Builder msg = JJCMessage.ResBuyJJCTimes.newBuilder();
        msg.setBuyTimes(player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.JJC.getValue(), 0));
        MessageUtils.send_to_player(player, JJCMessage.ResBuyJJCTimes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqChallenge(Player player, JJCMessage.ReqChallenge mess) {
        if (player.getId() == mess.getTargetID()) {
            return;
        }
        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }
        JJC jjc = Manager.jjcManager.getAlls().get(mess.getTargetID());
        if (jjc == null) {
            logger.error("没有找到挑战对象 TargetId=" + mess.getTargetID() + player);
            return;
        }

        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.JJC.getValue());
        if (remainCount == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PERSONBOSS_TIAOZHANNONUMVIP);
            return;
        }
        long now = TimeUtils.Time();
        if (Manager.jjcManager.getChallengedLock().containsKey(mess.getTargetID())) {
            long chtime = Manager.jjcManager.getChallengedLock().get(mess.getTargetID());
            if (now - chtime < 600000) {
                //正在被挑战
                logger.error("等待冷却至0" + mess.getTargetID() + " 正在被挑战！结束时间=" + TimeUtils.format2string(Manager.jjcManager.getChallengedLock().get(mess.getTargetID())));
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.JJCWAITCHALLEGEOVER);
                return;
            }
        }
        if (Manager.jjcManager.getChallengedLock().containsKey(player.getId())) {
            long chtime = Manager.jjcManager.getChallengedLock().get(player.getId());
            if (now - chtime < 600000) {
                //正在被挑战
                logger.error("等待冷却至0" + player.nameIdString() + " 正在被挑战！ 结束时间=" + TimeUtils.format2string(Manager.jjcManager.getChallengedLock().get(player.getId())));
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.JJCWAITCHALLEGEOVERSELF);
                return;
            }
        }

        Robot robot = jjc.init();
        if (robot == null) {
            logger.error("机器人为空 target={}, player={}", mess.getTargetID(), player);
            return;
        }
        //处理秒杀的情况
        if (mess.getSeckill()) {
            try {
                if (player.getFightPoint() <= robot.getFightPoint()) {
                    return;
                }
                Manager.countManager.addVariant(player, VariantType.Daily_JJC_Times, 1);
                Manager.jjcManager.scriptclone().onRobotDieBySecondKill(JJCManager.modelId, robot, player);
            } catch (Exception exception) {
                logger.error("JJC秒杀出错", exception);
            }
            return;
        }
        Manager.countManager.addVariant(player, VariantType.Daily_JJC_Times, 1);
        //TODO 如果当前地图模型id是8000，那么说明是持续挑战，不需要创建jjc副本，如果不是8000，则创建
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        if (null != mapObject && JJCManager.modelId == mapObject.getMapModelId()) {
            try {
                clearJJCInfo(mapObject);
                getReadyForBattle(player, robot, mapObject);
                Manager.jjcManager.scriptclone().keepFighting(player, mapObject);
            } catch (Exception exception) {
                logger.error(exception);
            }
            return;
        }
        //TODO 第一波
        onCreate(player, robot, JJCManager.modelId);
    }

    /**
     * 一键扫荡
     * @param player
     */
    public void onReqOneKeySweep(Player player){
        try {
            if ( !Manager.vipManager.power().canFree(player, VipPower.POWER_40)){
                logger.error("VIP等级不足够");
                return;
            }

            int canBuyNum = Manager.vipManager.power().getVipPurNum(player, VipPower.POWER_23);
            int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.JJC.getValue(), 0);
            if (buyCount < canBuyNum) {
                int gold = 0;
                int count = 0;
                for (; buyCount<canBuyNum;){
                    buyCount+=1;
                    gold += Manager.vipManager.power().getVipAddNumPrice(buyCount, VipPower.POWER_23);
                    count++;
                }

                if (!Manager.currencyManager.manager().decBindGoldOrGold(player, gold, ItemChangeReason.JJCBuyCountGetDec, IDConfigUtil.getLogId())) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.GoldNotEnough);
                    return;
                }
                player.getDailyActiveData().getDailyBuyCount().put(DailyActiveDefine.JJC.getValue(), buyCount);
                //资源找回记录购买册数
                Manager.retrieveResManager.getScript().addVipBuyCount(player,DailyActiveDefine.JJC.getValue(),count);
            }

            int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.JJC.getValue());
            if (remainCount <=0){
                logger.error("扫荡次数不足");
                return;
            }
            Manager.countManager.addVariant(player, VariantType.Daily_JJC_Times, remainCount);
            Manager.jjcManager.scriptclone().OneKeySweep(player,remainCount);
        }catch (Exception exception) {
            logger.error("扫荡出错", exception);
        }
    }

    @Override
    public void OnReqChangeTarget(Player player, JJCMessage.ReqChangeTarget mess) {

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.JJCChange, null)) {
            long cd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.JJCChange, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, String.valueOf((cd + 999) / 1000));
            return;
        }
        sendBattleList(player);
    }

    //发送挑战列表
    private void sendBattleList(Player player) {

        ResUpdatePlayers.Builder msg = ResUpdatePlayers.newBuilder();
        //TODO:判断这里是否有问题
        List<Integer> targets = getTargets(player, false);
        if (targets.isEmpty()) {
            MessageUtils.send_to_player(player, ResUpdatePlayers.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        ConcurrentHashMap<Integer, JJC> ranks = Manager.jjcManager.getAllRanks();

        for (int m : targets) {
            JJCobject.Builder member = getTarget(ranks, m, player);
            if (member == null) {
                logger.error(player.nameIdString() + " ,选择m =" + m + " 不存在！");
                continue;
            }
            msg.addPlayers(member);
        }

        JJCobject.Builder member1 = buildRank123(ranks, 1);
        if (null != member1) {
            msg.addRank123(member1);
        }
        JJCobject.Builder member2 = buildRank123(ranks, 2);
        if (null != member2) {
            msg.addRank123(member2);
        }
        JJCobject.Builder member3 = buildRank123(ranks, 3);
        if (null != member3) {
            msg.addRank123(member3);
        }

        MessageUtils.send_to_player(player, ResUpdatePlayers.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqGetAward(Player player, JJCMessage.ReqGetAward mess) {
        long count = Manager.countManager.getVariant(player, VariantType.JJCrewardCd);
        if (count >= 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.JJCRewardCd);
            OnReqGetYesterdayRank(player, null);
            return;
        }
        Cfg_JJCAeward_Bean config = getRewardConfig(player);
        if (config == null) {
            logger.error(player.nameIdString() + "竞技场的奖励数据没有找到！");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CanNotFindConfig);
            return;
        }
        List<Item> items = Item.createItems(config.getReward());

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.JJCRewardGet, IDConfigUtil.getLogId());
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.JJCAward, MessageString.JJCAward, items, ItemChangeReason.JJCRewardGet);
        }

        Manager.countManager.addVariant(player, VariantType.JJCrewardCd, 1);

        OnReqGetYesterdayRank(player, null);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.JJC, ItemChangeReason.JJCRewardGet);
    }

    private void newPlayerInit(Player player) {
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        if (jjc != null)
            return;
        jjc = new JJC();
        jjc.setRoleId(player.getId());
        jjc.setScore(0);
        jjc.setCareer(player.getCareer());
        //jjc.init();
        JJCDao mysql = new JJCDao();
        insert(jjc);
        mysql.insert(jjc);
        int rank = jjc.getScore();
        //新玩家，此时需要设置历史最高排名，以防止首次达到排名奖励出问题
        setPlayerJJCHistoryRank(player, rank);
    }

    @Override
    public void onReqGetFirstReward(Player player, JJCMessage.ReqGetFirstReward mess) {
        if (null == player) {
            return;
        }
        if (mess == null) {
            newPlayerInit(player);
            JJCMessage.ResGetFirstReward.Builder builder = JJCMessage.ResGetFirstReward.newBuilder();
            builder.setRank(player.getJjcHistoryMaxRank());
            builder.addAllRewardList(player.getJjcFirstRewardList());
            MessageUtils.send_to_player(player, JJCMessage.ResGetFirstReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            OnReqGetYesterdayRank(player, null);
        } else {

            int id = mess.getExcelId();
            if (id > player.getJjcHistoryMaxIndex()) {
                logger.error("玩家：" + player.getId() + " 领取竞技场首次排名奖励：" + id + " 大于了玩家历史最高奖励：" + player.getJjcHistoryMaxIndex());
                return;
            }
            if (player.getJjcFirstRewardList().contains(id)) {
                logger.error("玩家：" + player.getId() + " 已经领取过竞技场首次排名奖励：" + id);
                return;
            }
            player.getJjcFirstRewardList().add(id);
            sendFirstReward(player, id);
            JJCMessage.ResGetFirstReward.Builder builder = JJCMessage.ResGetFirstReward.newBuilder();
            builder.setRank(player.getJjcHistoryMaxRank());
            builder.addAllRewardList(player.getJjcFirstRewardList());
            MessageUtils.send_to_player(player, JJCMessage.ResGetFirstReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

            //红点刷新
            sendRewardRedPoint(player);
        }
    }

    //获取奖励配置
    private Cfg_JJCAeward_Bean getRewardConfig(Player player) {

        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        int rank = jjc.getScore();
        if (rank <= 0) {
            return null;
        }

        for (Cfg_JJCAeward_Bean config : CfgManager.getCfg_JJCAeward_Container().getValuees()) {

            if (config.getRank() == null || config.getRank().size() == 0) {
                continue;
            }

            if (config.getRank().size() == 1) {
                int min = config.getRank().get(0);
                if (min == rank) {
                    return config;
                }
                continue;
            }

            if (config.getRank().size() == 2) {
                int min = config.getRank().get(0);
                int max = config.getRank().get(1);
                max = max == -1 ? 99999999 : max;
                if (rank >= min && rank <= max) {
                    return config;
                }
            }
        }
        return null;
    }

    @Override
    public void setPlayerJJCHistoryRank(Player player, int newRank) {
        player.setJjcHistoryMaxRank(newRank);

        Cfg_JJCRank_Bean bean = Arrays.stream(CfgManager.getCfg_JJCRank_Container().getValuees()).parallel()
                .filter(tempBean -> tempBean.getPos_mix() <= newRank && tempBean.getPos_max() >= newRank)
                .findAny().get();
        if (bean.getId() > player.getJjcHistoryMaxIndex()) {
            player.setJjcHistoryMaxIndex(bean.getId());
        }
    }

    public void sendRewardRedPoint(Player player) {
        List<Integer> sort = player.getJjcFirstRewardList();
        boolean isNeedSend;
        if (sort.size() > 0) {
            Collections.sort(sort);
            Collections.reverse(sort);
            int maxIndex = sort.get(0);
            isNeedSend = player.getJjcHistoryMaxIndex() > maxIndex;
        } else {
            isNeedSend = player.getJjcHistoryMaxIndex() > 0;
        }
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.JJC.getValue());
        ResOnlineJJCInfo.Builder msg = ResOnlineJJCInfo.newBuilder();
        msg.setRCount(remainCount);
        msg.setAward(isNeedSend);
        MessageUtils.send_to_player(player, ResOnlineJJCInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqGetReport(Player player, JJCMessage.ReqGetReport mess) {

        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        if (jjc == null) {
            return;
        }

        ResReports.Builder msg = ResReports.newBuilder();

        for (JJCReport report : jjc.records) {
            Report.Builder mRoport = Report.newBuilder();
            mRoport.setTime(report.getTime());
            StringBuilder sb = new StringBuilder();
            boolean isMark = Utils.getMarkAfterString(report.getTarget(), sb);
            mRoport.setNameMark(isMark);
            mRoport.setName(sb.toString());
            mRoport.setType(report.isSucc() ? 1 : 0);
            mRoport.setLastRank(report.getLastRank());
            mRoport.setRank(report.getRank());
            mRoport.setTiaozhao(report.isTiaozhao());
            msg.addReports(mRoport);
        }

        MessageUtils.send_to_player(player, ResReports.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    @Override
    public void OnReqGetYesterdayRank(Player player, JJCMessage.ReqGetYesterdayRank mess) {
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        if (jjc == null) {
            return;
        }

        int rank = jjc.getScore();
        long isCd = Manager.countManager.getVariant(player, VariantType.JJCrewardCd);
        long cd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.JJCrewardCd, null);
        ResYesterdayRank.Builder msg = ResYesterdayRank.newBuilder();
        msg.setRank(rank);
        msg.setState(isCd > 0 ? 1 : 0);
        msg.setTime(isCd > 0 ? (int) (cd + 999) / 1000 : 0);
        MessageUtils.send_to_player(player, ResYesterdayRank.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqOpenJJC(Player player, JJCMessage.ReqOpenJJC mess) {
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        //如果是新玩家，无法找到比他排名还低的玩家/机器人，所以，新玩家在找enemy的时候不做加法
        boolean newPlayerFlag = false;
        if (jjc == null) {
            jjc = new JJC();
            newPlayerFlag = true;
            jjc.setRoleId(player.getId());
            jjc.setCareer(player.getCareer());
            jjc.setScore(0);
            JJCDao mysql = new JJCDao();
            insert(jjc);
            mysql.insert(jjc);
        }
        ConcurrentHashMap<Integer, JJC> ranks = Manager.jjcManager.getAllRanks();//Manager.jjcManager.getRanks(careerId(player));
        int rank = jjc.getScore();
        if (newPlayerFlag) {
            //新玩家，此时需要设置历史最高排名，以防止首次达到排名奖励出问题
            setPlayerJJCHistoryRank(player, rank);
        }
        sendJJCBuyTime(player);
        //修改因为时间的修改导致开始时间超过了当前时间
        if (player.getJjcStartTime() > TimeUtils.Time()) {
            player.setJjcStartTime(TimeUtils.Time());
        }
        long cd = player.endJjcCdTime();
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.JJC.getValue());
        ResOpenJJCresult.Builder msg = ResOpenJJCresult.newBuilder();
        msg.setRank(rank);
        msg.setScore(0);
        msg.setCount(remainCount);
        msg.setCd((int) (cd + 999) / 1000);

        List<Integer> targets = getTargets(player, newPlayerFlag);
        if (targets.isEmpty()) {
            MessageUtils.send_to_player(player, ResOpenJJCresult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        for (int m : targets) {
            JJCobject.Builder member = getTarget(ranks, m, player);
            if (member == null) {
                continue;
            }
            msg.addPlayers(member);
        }
        JJCobject.Builder member1 = buildRank123(ranks, 1);
        if (null != member1) {
            msg.addRank123(member1);
        }
        JJCobject.Builder member2 = buildRank123(ranks, 2);
        if (null != member2) {
            msg.addRank123(member2);
        }
        JJCobject.Builder member3 = buildRank123(ranks, 3);
        if (null != member3) {
            msg.addRank123(member3);
        }
        MessageUtils.send_to_player(player, ResOpenJJCresult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    public JJCobject.Builder getTarget(ConcurrentHashMap<Integer, JJC> ranks, int rank, Player player) {
        JJC jjc = ranks.get(rank);
        if (jjc == null) {
            logger.error("排行榜找不到呀：" + rank);
            JJC firstjjc = ranks.values().iterator().next();
            jjc = resetRobot(careerId(firstjjc), rank, firstjjc.getCareer());
            if (jjc == null) {
                return null;
            }
        }
        Robot robot = jjc.init();
        if (robot == null)
            return null;

        JJCobject.Builder msg = JJCobject.newBuilder();
        msg.setRoleID(jjc.getRoleId());
        msg.setRank(rank);
        msg.setCareer(jjc.getCareer());
        msg.setStateLv(robot.getStateLv());
        msg.setLevel(robot.getLevel());
        msg.setFightPower(robot.getFightPoint());
        msg.setFacade(MapUtils.getFacade(robot.getWingId(), robot.getFashionBodyId(), robot.getFashionHalo(), robot.getFashionMatrix(), robot.getFashionWeaponId(), robot.getSpiritId(), robot.getSoulArmorId()));

        StringBuilder sb = new StringBuilder();
        boolean isMark = Utils.getMarkAfterString(robot.getName(), sb);
        msg.setNameMark(isMark);
        msg.setName(sb.toString());
        boolean secSkillFlag;
        int count = 0;
        if (null == player) {
            secSkillFlag = false;
            count = 0;
        } else {
            secSkillFlag = secKill(player.getFightPoint(), robot.getFightPoint());
            count = (int) Manager.countManager.getVariant(player, VariantType.JJCAccumulationCount);
        }
        secSkillFlag = count >= Global.JJC_Auto_Time_Limit ? secSkillFlag : false;
        msg.setSeckill(secSkillFlag);
        return msg;

    }

    /**
     * 竞技场选对手规则
     * 根据自己的名次在jjcrank.xlsx表中找到对应的配置
     * 根据配置中enemy1、2、3的选取规则，找到3个enemy
     */
    private List<Integer> getTargets(Player player, boolean newPlayerFlag) {
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        int rank;
        ConcurrentHashMap<Integer, JJC> ranks = Manager.jjcManager.getAllRanks();
        if (jjc == null) {
            rank = ranks.size() - 1;
        } else {
            rank = jjc.getScore();
        }
        final int tempRank = rank;

        Cfg_JJCRank_Bean jjcRankBean = Arrays.stream(CfgManager.getCfg_JJCRank_Container().getValuees()).parallel()
                .filter(bean -> bean.getPos_max() >= tempRank)
                .filter(bean -> bean.getPos_mix() <= tempRank)
                .findAny()
                .orElse(null);
        if (null == jjcRankBean) {
            return null;
        }

        List<Integer> targets = new ArrayList<>();
        /**
         * 按照策划的构想，enemy1和enemy2应该是在[小于玩家排名，大于玩家排名]中选择，因此minDiff应该是负数
         * enemy3则是[大于玩家排名，大于玩家排名]中选择
         * */

        int count = (int) Manager.countManager.getVariant(player, VariantType.JJCAccumulationCount);
        if (newPlayerFlag || count < 1) {
            //策划特殊要求 第一次打开竞技场，只能刷机器人，切战力还比自己低的，所以特殊处理了
            for (int start = ranks.size() - 1; start >= 0; start--) {
                JJC jjc1 = ranks.get(start);
                if (jjc1 == null) {
                    logger.error("竞技场当前排名为空 {} ", start);
                    continue;
                }
                if (jjc1.getRoleId() > 5000001)
                    continue;
                targets.add(start);
                if (targets.size() >= 3)
                    break;
            }
            //targets.add(getRandomRank(rank, 0, -jjcRankBean.getEnemy1r(), targets));
            //targets.add(getRandomRank(rank, 0, -jjcRankBean.getEnemy2r(), targets));
            //targets.add(getRandomRank(rank, 0, -jjcRankBean.getEnemy3r(), targets));
        } else {
            int cap = ranks.size() - rank;
            targets.add(getRandomRank(rank, -jjcRankBean.getEnemy3(), -jjcRankBean.getEnemy3r(), targets));

            if (cap < jjcRankBean.getEnemy2()) {
                targets.add(getRandomRank(rank, cap, -jjcRankBean.getEnemy2r(), targets));
            } else {
                targets.add(getRandomRank(rank, jjcRankBean.getEnemy2(), -jjcRankBean.getEnemy2r(), targets));
            }

            if (cap < jjcRankBean.getEnemy1()) {
                targets.add(getRandomRank(rank, cap, -jjcRankBean.getEnemy1r(), targets));
            } else {
                targets.add(getRandomRank(rank, jjcRankBean.getEnemy1(), -jjcRankBean.getEnemy1r(), targets));
            }
        }
        Collections.sort(targets);
        Collections.reverse(targets);
        return targets;
    }

    @Override
    public void Online(Player player) {
        sendRewardRedPoint(player);
    }

    @Override
    public void loadAll() {
        //加载机器人竞技场
        loadAllRobot();
        //加载玩家竞技场
        JJCDao handler = new JJCDao();
        List<JJCBean> all = handler.selectAll();

        HashMap<Integer, Long> tlist = new HashMap<>();
        HashMap<Integer, Long> notInRobotlist = new HashMap<>();
        int notInRootRankCount = 0;
        for (JJCBean bean : all) {
            JJC jjc = new JJC(bean);
            Manager.jjcManager.getAlls().put(jjc.getRoleId(), jjc);
            int score = jjc.getScore();
            /**
             * 由于玩家的竞技场数据会替换到相应排名机器人竞技场的数据
             * 所以，需要将被替换的机器人竞技场的数据保存，以便后续扩大整个竞技场数据池
             * */
            JJC robotJJC = Manager.jjcManager.getAllRanks().get(score);
            if (null != robotJJC) {
                tlist.put(score, robotJJC.getRoleId());
            } else {
                notInRobotlist.put(score, jjc.getRoleId());
                notInRootRankCount++;
            }

            Manager.jjcManager.getAllRanks().put(score, jjc);
        }
        if (tlist.size() <= 0) {
            return;
        }
        List<Integer> sort = new ArrayList<>(tlist.keySet());
        Collections.sort(sort);
        HashMap<Integer, Integer> sorlist = new HashMap<>();
        int rankStartIndex = Manager.jjcManager.getAllRanks().size() - notInRootRankCount + 1;
        for (int k = 0; k < sort.size(); ++k) {
            int rankindex = rankStartIndex + k;
            while (notInRobotlist.containsKey(rankindex)) {
                rankindex++;
            }
            notInRobotlist.put(rankindex, tlist.get(sort.get(k)));
            sorlist.put(rankindex, sort.get(k));
        }

        for (Entry<Integer, Integer> men : sorlist.entrySet()) {
            int key = men.getKey();
            int oldkey = men.getValue();
            if (key == oldkey) {
                continue;
            }
            long roleId = tlist.get(oldkey);
            JJC jjc = Manager.jjcManager.getAlls().get(roleId);
            jjc.setScore(key);
            Manager.jjcManager.getAllRanks().put(key, jjc);
            logger.info(roleId + "由排行第" + oldkey + " 变化为新的" + key);
        }
    }

    //加载所有机器人
    private void loadAllRobot() {
        for (Cfg_Jjcrobot_Bean config : CfgManager.getCfg_Jjcrobot_Container().getValuees()) {
            int robotId = config.getRobotID() * 10000;
            for (int i = config.getRank_mix(); i <= config.getRank_max(); i++) {
                JJC jjc = new JJC();
                jjc.setRoleId(robotId + i);
                jjc.setCareer(config.getCareer());
                jjc.setCamp(config.getCamp());
//                int score = config.getRank_max();
                jjc.setScore(i);
                //jjc.init();
                Manager.jjcManager.getAlls().put(jjc.getRoleId(), jjc);
                Manager.jjcManager.getAllRanks().put(i, jjc);
            }
        }
    }

    private long careerId(JJC jjc) {
        return jjc.getCareer();
    }

    //插入新玩家
    private void insert(JJC jjc) {
        if (Manager.jjcManager.getAlls().containsKey(jjc.getRoleId())) {
            logger.error("重复插入uid=" + jjc.getRoleId());
            return;
        }

        Manager.jjcManager.getAlls().put(jjc.getRoleId(), jjc);

        ConcurrentHashMap<Integer, JJC> ranks = Manager.jjcManager.getAllRanks();
        int lastId = 0;
        for (int key : ranks.keySet()) {
            if (lastId < key) {
                lastId = key;
            }
        }
        lastId += 1;
        jjc.setScore(lastId);
        Manager.jjcManager.getAllRanks().put(lastId, jjc);
    }

    @Override
    public void sort(int newsort, JJC hero) {
        if (newsort < 0) {
            return;
        }
        ConcurrentHashMap<Integer, JJC> ranks = Manager.jjcManager.getAllRanks();
        if (ranks.size() <= 1) {
            return;
        }
        int beginSort = hero.getScore();
        if (newsort >= beginSort) {
            return;
        }

        //TODO 进行排名互换
        JJC other = ranks.get(newsort);
        other.setScore(beginSort);
        hero.setScore(newsort);

        ranks.put(other.getScore(), other);
        ranks.put(hero.getScore(), hero);
        if (other.getRoleId() > 5000001) {
            Player oterPlayer = PlayerManager.getInstance().getPlayer(other.getRoleId());
            if (oterPlayer != null)
                Manager.rankListManager.deal().setArenaRank(oterPlayer, beginSort);
        }
        JJCDao handler = new JJCDao();
        handler.update(hero);
        handler.update(other);
    }

    //创建擂台
    private void onCreate(Player player, Robot robot, int modelId) {
        MapObject mapObject = Manager.mapManager.createCopyMap(modelId, 1, player.getId());
        getReadyForBattle(player, robot, mapObject);
        Manager.mapManager.changeMap(player, mapObject.getId(), null, false);
    }


    private void clearJJCInfo(MapObject mapObject) {
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_Robert, null);
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_Player, null);
    }

    private void getReadyForBattle(Player player, Robot robot, MapObject mapObject) {
        long now = TimeUtils.Time();
        Manager.jjcManager.getChallengedLock().put(player.getId(), now);
        Manager.jjcManager.getChallengedLock().put(robot.getId(), now);
        player.setCurHp(player.getAttribute().MaxHP());

        Position robotPost = mapObject.getBriths().get(1);
        robot.changeCurPos(robotPost);
        robot.changeMapId(mapObject.getId());
        robot.changeLine(mapObject.getLineId());
        robot.changeMapModelId(mapObject.getMapModelId());
        robot.reset();
        robot.onHpChange(robot);
        robot.setAi(RobotAi.JJC);

        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_Robert, robot);
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_Player, player);

        ResStartBattleRes.Builder succ = ResStartBattleRes.newBuilder();
        MessageUtils.send_to_player(player, ResStartBattleRes.MsgID.eMsgID_VALUE, succ.build().toByteArray());
        logger.error(player.nameIdString() + "正在挑战对象 TargetId=" + robot.getName() + " id=" + robot.getId());
    }

    @Override
    public void onDelete(Player player, int oldCareer) {
        //职业是相同的， 就不需要操作
        if (oldCareer == player.getCareer()) {
            return;
        }
        try {
            JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
            Manager.jjcManager.getAlls().remove(player.getId());
            int rank = jjc.getScore();

            Manager.jjcManager.getAllRanks().remove(rank);
            resetRobot(oldCareer, rank, oldCareer);
            logger.error(player.nameIdString() + "清除排名为:" + rank);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    private JJC resetRobot(long ccID, int rank, int oldCareer) {
        //排名数据重置
        for (Cfg_Jjcrobot_Bean config : CfgManager.getCfg_Jjcrobot_Container().getValuees()) {
            //TODO：修改这里的排名对比方式
            if (config.getRank_max() != rank) {
                continue;
            }
            if (config.getCareer() != oldCareer) {
                continue;
            }

            JJC jjc = new JJC();
            jjc.setRoleId(config.getRobotID());
            jjc.setCareer(config.getCareer());
            jjc.setCamp(config.getCamp());
            int index = config.getRank_max();
            jjc.setScore(index);
            Manager.jjcManager.getAlls().put(jjc.getRoleId(), jjc);

            Manager.jjcManager.getAllRanks().put(index, jjc);
            return jjc;
        }
        return null;
    }

    /***************************************private functions**************************************/
    private JJCobject.Builder buildRank123(ConcurrentHashMap<Integer, JJC> ranks, int index) {
        if (ranks.size() >= index) {
            return getTarget(ranks, index, null);
        }
        return null;
    }

    /**
     * higherThanMyRank 比我高多少名，比如我是50名，那么比我高20名就是30名
     * lowerThanMyRank 反之
     */
    private int getRandomRank(int playerRank, int lowerThanMyRank, int higherThanMyRank, List<Integer> targets) {

        int max = playerRank + lowerThanMyRank - 1;
        max = max <= 0 ? 1 : max;
        int min = playerRank + higherThanMyRank;
        min = min <= 0 ? 1 : min;
        int result = RandomUtils.random(min, max);
        if (result == playerRank) {
            int i = 3;
            while (i > 0) {
                i--;
                max = min == max ? max + 10 : max;
                result = RandomUtils.random(min, max);
                if (result != playerRank) {
                    break;
                }
            }
        }
        List<Integer> newTargets = new ArrayList<>();
        newTargets.add(playerRank);
        newTargets.addAll(targets);
        int count = 0;
        while (newTargets.contains(result) && count < 10) {
            max = min == max ? max + 10 : max;
            result = RandomUtils.random(min, max);
            count++;
            if (count >= 10)
                logger.error("JJCRank配置有错 min " + min + "  max  " + max);
        }
        return result;
    }


    /**
     * 发送首次排名的奖励
     */
    private void sendFirstReward(Player player, int excelId) {
        Cfg_JJCRank_Bean bean = CfgManager.getCfg_JJCRank_Container().getValueByKey(excelId);
        if (null == bean) {
            return;
        }

        List<Item> items = Item.createItems(bean.getFirst_reward_item());
        long actionId = IDConfigUtil.getLogId();
        if (items != null && !items.isEmpty()) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.JJCFirstRewardGet, actionId);

            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.JJC, ItemChangeReason.JJCFirstRewardGet);
        }
    }

    /**
     * 判断是否秒杀对方
     */
    private boolean secKill(long power, long targetPower) {
        long diff = power - targetPower;
        if (diff <= 0) {
            return false;
        }
        float percentage = (((float) power / targetPower) - 1) * 100;
        return percentage > Global.JJc_fighting_kill;
    }

    //竞技场排行奖励
    @Override
    public void sendArenaRankReward(ConcurrentHashMap<Integer, Long> rankMap) {
        try {
            ConcurrentHashMap<Long, JJC> ranks = JJCManager.getInstance().getAlls();
            for (Long id : rankMap.values()) {
                RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(id);
                if (rankPlayer == null) {
                    continue;
                }

                for (Cfg_JJCAeward_Bean bean : CfgManager.getCfg_JJCAeward_Container().getValuees()) {
                    ReadIntegerArray rankRangeArray = bean.getRank();
                    JJC player = ranks.get(rankPlayer.getRoleId());
                    if (player == null)
                        continue;
                    int rank = player.getScore();
                    //排名范围大小为1的情况，目前只有第一名
                    if (rankRangeArray.size() == 1) {
                        if (rank == rankRangeArray.get(0)) {
                            List<Item> items = Item.createItems(bean.getReward(), 1);
                            Manager.mailManager.sendMailToPlayer(rankPlayer.getRoleId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.JJC_DREWARD_MAIL, MessageString.JJC_DREWARD_CONTEN, items, ItemChangeReason.JJCRankGet);
                            break;
                        }
                    } else {
                        if (rank >= rankRangeArray.get(0)) {
                            if (rank <= rankRangeArray.get(1)) {
                                List<Item> items = Item.createItems(bean.getReward(), 1);
                                Manager.mailManager.sendMailToPlayer(rankPlayer.getRoleId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.JJC_DREWARD_MAIL, MessageString.JJC_DREWARD_CONTEN, items, ItemChangeReason.JJCRankGet);
                                break;
                            }

                            if (-1 == rankRangeArray.get(1)) {
                                List<Item> items = Item.createItems(bean.getReward(), 1);
                                Manager.mailManager.sendMailToPlayer(rankPlayer.getRoleId(), MailType.SysCommonRewardMail, MessageString.System, MessageString.JJC_DREWARD_MAIL, MessageString.JJC_DREWARD_CONTEN, items, ItemChangeReason.JJCRankGet);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }
}
