package common.peak;

import com.data.*;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_PeakBattleJoinReward_Bean;
import com.data.bean.Cfg_PeakBattleStage_Bean;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.PeakBean;
import com.game.db.dao.PeakDao;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.peak.script.IPeakPk;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.thread.SaveServer;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.PeakMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2020/10/14 14:20
 * @Auth ZUncle
 */
public class PeakPkScript implements IPeakPk {

    final Logger logger = LogManager.getLogger(PeakPkScript.class);
    final int MaxStage = 5; //最高段位
    final int CloneId = 8100;       //巅峰竞技副本
    final int WaitCloneId = 8101;   //匹配等待副本
    final PeakDao peakDao = new PeakDao();

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PeakPkScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 取消匹配
     *
     * @param player
     */
    @Override
    public void reqCancelPeakMatch(Player player) {

        if (player == null) {
            return;
        }
        if (Manager.peakManager.getMatch() == null) {
            return;
        }

        //取消跨服匹配
        if (isCrossPeakOpen()) {
            PeakMessage.G2PCancelPeakMatch.Builder message = PeakMessage.G2PCancelPeakMatch.newBuilder();
            message.setRoleId(player.getId());
            MessageUtils.send_to_public(PeakMessage.G2PCancelPeakMatch.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }
        PeakBean peak = Manager.peakManager.getPeaks().get(player.getId());
        if (peak == null || peak.isFight()) {
            return;
        }

        PeakBean m = Manager.peakManager.getMatch().remove(peak.getRoleId());
        if (m != null) {
            sendCancelMatch(player);
            logger.info("巅峰竞技.取消匹配 player={}", player);
        }
    }

    /**
     * 发送取消匹配
     *
     * @param player
     */
    void sendCancelMatch(Player player) {
        if (player == null) {
            return;
        }
        PeakMessage.ResCancelPeakMatch.Builder message = PeakMessage.ResCancelPeakMatch.newBuilder();
        MessageUtils.send_to_player(player, PeakMessage.ResCancelPeakMatch.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 匹配
     *
     * @param player
     */
    @Override
    public void reqEnterPeakMatch(Player player) {


        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ThisFunctionIsUnlock);
            return;
        }
        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }

        if (Manager.peakManager.getMatch() == null) {
            sendCancelMatch(player);
            //TODO 活动未开启
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Daily_Not_In_Time);
            return;
        }

        //进入跨服匹配
        if (isCrossPeakOpen()) {
            if (Manager.publicServerManager.getPublicSession() == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.PublicServer_Close);
                return;
            }
            PeakMessage.G2PEnterPeakMatch.Builder message = PeakMessage.G2PEnterPeakMatch.newBuilder();
            message.setRole(pack(player));
            MessageUtils.send_to_public(PeakMessage.G2PEnterPeakMatch.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        PeakBean peak = Manager.peakManager.getPeaks().get(player.getId());
        if (peak == null) {
            peak = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(peak, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }
        if (peak.isFight()) {
            return;
        }
        peak.setPower(player.getFightPoint());
        int delay = RandomUtils.random(Global.PeakBattle_MatchServerTime.get(0, 10), Global.PeakBattle_MatchServerTime.get(1, 60)) * 1000;
        peak.setDelay(TimeUtils.Time() + delay);
        Manager.peakManager.getMatch().put(peak.getRoleId(), peak);

        PeakMessage.ResPeakMatchRes.Builder message = PeakMessage.ResPeakMatchRes.newBuilder();
        MessageUtils.send_to_player(player, PeakMessage.ResPeakMatchRes.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("巅峰竞技.匹配 player={}", player);
    }

    PeakBean init(Player player) {
        PeakBean peak = new PeakBean();
        peak.setRoleId(player.getId());
        peak.setRankId(1);
        Manager.peakManager.getPeaks().put(peak.getRoleId(), peak);
        Manager.peakManager.getRanks().add(peak);
        return peak;
    }

    /**
     * 获取巅峰数据
     *
     * @param player
     */
    @Override
    public void reqPeakInfo(Player player) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            return;
        }

        long total = Manager.countManager.getVariant(player, VariantType.PeakTotal);
        long win = Manager.countManager.getVariant(player, VariantType.PeakWin);
        long exp = Manager.countManager.getVariant(player, VariantType.Peak_DayPkExp);

        if (isCrossPeakOpen()) {
            PeakMessage.G2PPeakInfo.Builder message = PeakMessage.G2PPeakInfo.newBuilder();
            message.setRole(pack(player));
            message.setAll((int) total);
            message.setWin((int) win);
            message.setDayExp(exp);
            MessageUtils.send_to_public(PeakMessage.G2PPeakInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        PeakBean peak = Manager.peakManager.getPeaks().get(player.getId());
        if (peak == null) {
            peak = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(peak, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }

        PeakMessage.ResPeakInfo.Builder message = PeakMessage.ResPeakInfo.newBuilder();
        message.setAll((int) total);
        message.setWin((int) win);
        message.setDayPkCount(peak.getDayTimes());
        message.setDayExp(exp);

        for (Cfg_PeakBattleJoinReward_Bean bean : CfgManager.getCfg_PeakBattleJoinReward_Container().getValuees()) {
            if (peak.checkTimesRewardState(bean.getId())) {
                message.addDayBoxIds(bean.getJoinTimes());
            }
        }
        MessageUtils.send_to_player(player, PeakMessage.ResPeakInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

        logger.info("请求巅峰竞技玩家数据 player={}", player);
    }

    /**
     * 获取排名信息
     *
     * @param player
     */
    @Override
    public void reqPeakRankInfo(Player player) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            return;
        }

        if (isCrossPeakOpen()) {
            PeakMessage.G2PPeakRankInfo.Builder message = PeakMessage.G2PPeakRankInfo.newBuilder();
            message.setRole(pack(player));
            MessageUtils.send_to_public(PeakMessage.G2PPeakRankInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        int selfOrder = 0;
        PeakMessage.ResPeakRankList.Builder message = PeakMessage.ResPeakRankList.newBuilder();
        message.setSeason(ServerParamUtil.peakSeason);
        for (int i = 0; i < Manager.peakManager.getRanks().size(); i++) {
            PeakBean bean = Manager.peakManager.getRanks().get(i);
            if (i < Global.PeakBattle_RankCount) {
                PeakMessage.PeakRankRole.Builder rank = pack(bean);
                rank.setOrder(i + 1);
                message.addRanks(rank);
            }
            if (bean.getRoleId() == player.getId()) {
                selfOrder = i + 1;
            }
        }
        PeakBean self = Manager.peakManager.getPeaks().get(player.getId());
        if (self == null) {
            self = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(self, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }
        PeakMessage.PeakRankRole.Builder selfRank = pack(self);
        selfRank.setOrder(selfOrder);
        message.setSelfRank(selfRank);

        MessageUtils.send_to_player(player, PeakMessage.ResPeakRankList.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("请求巅峰竞技排名数据 player={}", player);
    }

    PeakMessage.PeakCrossRole.Builder pack(Player player) {
        PeakMessage.PeakCrossRole.Builder message = PeakMessage.PeakCrossRole.newBuilder();
        message.setRoleId(player.getId());
        message.setName(player.getName());
        message.setPlatform(ServerConfig.getServerPlatform());
        message.setServerId(ServerConfig.getServerId());
        message.setPower(player.getFightPoint());
        return message;
    }

    PeakMessage.PeakRankRole.Builder pack(PeakBean bean) {
        PeakMessage.PeakRankRole.Builder builder = PeakMessage.PeakRankRole.newBuilder();
        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(bean.getRoleId());
        builder.setRoleId(bean.getRoleId());
        builder.setName(player.getRolename());
        builder.setServerId(ServerConfig.getServerId());
        builder.setScore(bean.getScore());
        builder.setStageId(bean.getRankId());
        builder.setPower(player.getFightPower());
        return builder;
    }

    /**
     * 获取段位信息
     *
     * @param player
     */
    @Override
    public void reqPeakStageInfo(Player player) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            return;
        }

        if (isCrossPeakOpen()) {
            PeakMessage.G2PPeakStageInfo.Builder message = PeakMessage.G2PPeakStageInfo.newBuilder();
            message.setRole(pack(player));
            MessageUtils.send_to_public(PeakMessage.G2PPeakStageInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }
        PeakBean peak = Manager.peakManager.getPeaks().get(player.getId());
        if (peak == null) {
            peak = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(peak, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }

        //已领奖励列表
        List<Integer> stageGetList = new ArrayList<>();
        for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
            if (peak.checkStageRewardState(config.getId())) {
                stageGetList.add(config.getId());
            }
        }
        PeakMessage.ResPeakStageInfo.Builder message = PeakMessage.ResPeakStageInfo.newBuilder();
        message.setScore(peak.getScore());
        message.addAllStageList(stageGetList);
        for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
            if (config.getLimitTimes() <= 0) {
                continue;
            }
            int use = Manager.countManager.getServerCount(BaseCountType.PeakWeekStageBox, config.getId());
            PeakMessage.PeakStageReward.Builder remain = PeakMessage.PeakStageReward.newBuilder();
            remain.setStateId(config.getId());
            remain.setCount(Math.max(0, config.getLimitTimes() - use));
            message.addRemainReward(remain);
        }
        MessageUtils.send_to_player(player, PeakMessage.ResPeakStageInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 领取巅峰竞技段位奖励
     *
     * @param player
     * @param stageId
     */
    @Override
    public void reqPeakStageReward(Player player, int stageId) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            return;
        }

        if (isCrossPeakOpen()) {
            if (Manager.publicServerManager.getPublicSession() == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.PublicServer_Close);
                return;
            }
            PeakMessage.G2PPeakStageReward.Builder message = PeakMessage.G2PPeakStageReward.newBuilder();
            message.setRole(pack(player));
            message.setStageId(stageId);
            MessageUtils.send_to_public(PeakMessage.G2PPeakStageReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }
        Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(stageId);
        if (config == null) {
            return;
        }

        PeakBean bean = Manager.peakManager.getPeaks().get(player.getId());
        if (bean == null) {
            bean = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }
        if (bean.getRankId() < stageId || bean.checkStageRewardState(config.getId())) {
            sendStageResult(player, config.getId(), false);
            return;
        }
        //TODO 检测奖励剩余份数
        if (config.getLimitTimes() > 0) {
            if (Manager.countManager.getServerCount(BaseCountType.PeakWeekStageBox, config.getId()) >= config.getLimitTimes()) {
                sendStageResult(player, config.getId(), false);
                return;
            }
            Manager.countManager.addServerCount(BaseCountType.PeakWeekStageBox, Count.RefreshType.CountType_Forever, config.getId(), 1);
        }
        bean.signStageRewardState(config.getId(), true);
        //派发奖励
        dispatchReward(player, config);

        sendStageResult(player, config.getId(), true);

        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.UPDATE);
    }


    /**
     * 领取巅峰竞技场次奖励
     *
     * @param player
     * @param times
     */
    @Override
    public void reqPeakTimesReward(Player player, int times) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.ArenaTop)) {
            return;
        }

        Cfg_PeakBattleJoinReward_Bean box = Utils.findOne(CfgManager.getCfg_PeakBattleJoinReward_Container().getValuees(), bean -> bean.getJoinTimes() == times);
        if (box == null) {
            return;
        }

        if (isCrossPeakOpen()) {
            PeakMessage.G2PPeakTimesReward.Builder message = PeakMessage.G2PPeakTimesReward.newBuilder();
            message.setRole(pack(player));
            message.setTimes(box.getId());
            MessageUtils.send_to_public(PeakMessage.G2PPeakTimesReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        PeakBean bean = Manager.peakManager.getPeaks().get(player.getId());
        if (bean == null) {
            bean = init(player);
            Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.INSERT);
        }
        if (times > bean.getDayTimes() || bean.checkTimesRewardState(box.getId())) {
            return;
        }
        bean.signTimesRewardState(box.getId(), true);
        //派发奖励
        dispatchReward(player, box);

        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.UPDATE);
    }

    /**
     * 跨服巅峰竞技挑战结果
     *
     * @param roleId
     * @param isWin
     * @param exp
     */
    @Override
    public void P2GPeakCloneResult(long roleId, int isWin, long exp) {
        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return;
        }
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.PeakPk, 1);
        Manager.countManager.addVariant(player, VariantType.PeakTotal, 1);
        Manager.countManager.addVariant(player, VariantType.Peak_DayPkExp, exp);
        Manager.countManager.addVariant(player, VariantType.Peak_DayBox, 1);
        if (isWin == 1) {
            Manager.countManager.addVariant(player, VariantType.PeakWin, 1);
        }
        Manager.controlManager.operate(player, FunctionVariable.Join_ArenaTop_Num, 1);
        logger.info("跨服巅峰竞技结果返回 player={}", player);
    }

    /**
     * 跨服领取段位奖励
     *
     * @param messInfo
     */
    @Override
    public void P2GPeakStageReward(PeakMessage.P2GPeakStageReward messInfo) {
        for (PeakMessage.RewardInfo info : messInfo.getInfoList()) {
            if (messInfo.getIsMail()) {
                sendStageMail(info.getRoleId(), info.getIdList());
                continue;
            }
            Player player = Manager.playerManager.getPlayerOnline(info.getRoleId());
            if (player == null) {
                sendStageMail(info.getRoleId(), info.getIdList());
                continue;
            }
            for (int timesId : info.getIdList()) {
                Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(timesId);
                if (config == null) {
                    continue;
                }
                dispatchReward(player, config);
                logger.info("跨服巅峰竞技领取段位奖励 stage={} player={}", config.getId(), player);
            }
        }
    }

    /**
     * 跨服领取场次奖励
     *
     * @param messInfo
     */
    @Override
    public void P2GPeakTimesReward(PeakMessage.P2GPeakTimesReward messInfo) {

        for (PeakMessage.RewardInfo info : messInfo.getInfoList()) {
            if (messInfo.getIsMail()) {
                sendTimesMail(info.getRoleId(), info.getIdList());
                continue;
            }
            Player player = Manager.playerManager.getPlayerOnline(info.getRoleId());
            if (player == null) {
                sendTimesMail(info.getRoleId(), info.getIdList());
                continue;
            }
            for (int timesId : info.getIdList()) {
                Cfg_PeakBattleJoinReward_Bean config = CfgManager.getCfg_PeakBattleJoinReward_Container().getValueByKey(timesId);
                if (config == null) {
                    continue;
                }
                dispatchReward(player, config);
                logger.info("跨服巅峰竞技领取场次奖励times={} player={}", config.getJoinTimes(), player);
            }
        }
    }

    /**
     * 创建匹配等待副本
     */
    @Override
    public MapObject getWaitMap() {

        if (Manager.peakManager.getMatch() == null) {
            return null;
        }

        MapObject map = Manager.mapManager.getMap(Manager.peakManager.getWaitId());
        if (map != null) {
            return map;
        }
        map = Manager.mapManager.createCopyMap(WaitCloneId, 1, WaitCloneId);
        Manager.peakManager.setWaitId(map.getId());
        return map;

    }

    // 派发奖励
    void dispatchReward(Player player, Cfg_PeakBattleJoinReward_Bean box) {
        List<Item> items = Item.createItems(box.getReward());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.PeekTimesRewardGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.PeekTimesRewardGet);
        }
        PeakMessage.ResPeakTimesResult.Builder message = PeakMessage.ResPeakTimesResult.newBuilder();
        message.setTimes(box.getJoinTimes());
        MessageUtils.send_to_player(player, PeakMessage.ResPeakTimesResult.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    // 派发奖励
    void dispatchReward(Player player, Cfg_PeakBattleStage_Bean config) {
        List<Item> items = Item.createItems(config.getStageReward());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.PeekStageRewardGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.PeekStageRewardGet);
        }
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.PeakStage, ItemChangeReason.PeekStageRewardGet, config.getId());
    }

    void sendStageResult(Player player, int stageId, boolean success) {

        Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(stageId);
        int remain = config.getLimitTimes() <= 0 ? -1 : config.getLimitTimes() - Manager.countManager.getServerCount(BaseCountType.PeakWeekStageBox, config.getId());

        PeakMessage.ResPeakStageResult.Builder message = PeakMessage.ResPeakStageResult.newBuilder();
        message.setStageId(stageId);
        message.setState(success);
        message.setRemain(remain);
        MessageUtils.send_to_player(player, PeakMessage.ResPeakStageResult.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 活动开启
     */
    @Override
    public void start() {
        Manager.peakManager.setMatch(new ConcurrentHashMap<>());
        GameServer.getInstance().getAssistThread().addTimerEvent(Manager.peakManager.getTimer());

        logger.info("开启巅峰竞技场活动=====>");
    }

    /**
     * 活动结束
     */
    @Override
    public void close() {
        GameServer.getInstance().getAssistThread().removeTimerEvent(Manager.peakManager.getTimer());
        if (Manager.peakManager.getMatch() == null) {
            return;
        }
        for (PeakBean bean : Manager.peakManager.getMatch().values()) {
            Player player = Manager.playerManager.getPlayerOnline(bean.getRoleId());
            sendCancelMatch(player);
        }

        MapObject waitMap = getWaitMap();
        Manager.mapManager.base(ScriptEnum.PeakWaitScript).call("activityEnd", waitMap);
        Manager.peakManager.setMatch(null);

        logger.info("关闭巅峰竞技场活动=====>");
    }

    /**
     * 开始匹配
     */
    @Override
    public void match() {
        if (Manager.peakManager.getMatch() == null) {
            return;
        }
        long curTime = TimeUtils.Time();
        List<PeakBean> queue = Utils.find(Manager.peakManager.getMatch().values(), p -> p.getDelay() < curTime);

        if (queue.size() < 2) {
            return;
        }
        /**
         * 将战力相近的优先匹配到一起（战力差值最小的匹配到一起）
         * 匹配到一起的玩家记录为上场匹配到的玩家，接下来的一场PK不会优先匹配到
         * 本段位 > 本段位+1段位 > 本段位-1段位 >本段位+2段位 >本段位-2段位 >本段位+3段位 > 本段位-3段位 >本段位+4段位 >本段位-4段位
         * 	连负玩家 匹配优先级：比自己战力低且连胜的玩家>比自己战力低的玩家>其他玩家
         * 	连胜玩家 匹配优先级：比自己战力高且连负的玩家>比自己战力高的玩家>其他玩家
         */
        queue.sort(Comparator.comparingInt(PeakBean::getScore).thenComparingLong(PeakBean::getPower).reversed());
        List<PeakBean> winLian = Utils.split(queue, o -> o.getWinLian() >= Global.PeakBattle_ContinueWin);
        List<PeakBean> loserLian = Utils.split(queue, o -> o.getWinLian() >= Global.PeakBattle_ContinueLost);
        //连胜匹配区间向上自增
        Iterator<PeakBean> iterator = winLian.iterator();
        while (iterator.hasNext()) {
            PeakBean next = iterator.next();
            if (match(next, queue, true, +1)) {
                iterator.remove();
                continue;
            }
            if (match(next, queue, false, +1)) {
                iterator.remove();
            }
        }
        logger.info("连胜匹配结束,剩余未匹配数量 len={}", Manager.peakManager.getMatch().size());
        //剩余连胜两两打，总有一个负
        match(winLian, 0, true);
        logger.info("连胜p2p匹配结束,剩余未匹配数量 len={}", Manager.peakManager.getMatch().size());
        //连服匹配区间向下自减
        iterator = loserLian.iterator();
        while (iterator.hasNext()) {
            PeakBean next = iterator.next();
            if (match(next, queue, true, -1)) {
                iterator.remove();
                continue;
            }
            if (match(next, queue, false, -1)) {
                iterator.remove();
            }
        }
        logger.info("连负匹配结束,剩余未匹配数量 len={}", Manager.peakManager.getMatch().size());
        //未匹配连胜连负回归队列
        queue.addAll(winLian);
        queue.addAll(loserLian);

        for (int i = 1; i <= MaxStage; i++) {
            final int index = i;
            List<PeakBean> level = Utils.split(queue, o -> o.getRankId() <= MaxStage * index);
            level.sort(Comparator.comparingInt(PeakBean::getScore).thenComparingLong(PeakBean::getPower).reversed());
            match(level, 0, true);
            logger.info("段位区间={}匹配结束,剩余未匹配数量 len={}", index, Manager.peakManager.getMatch().size());
            queue.addAll(level);
        }
        queue.sort(Comparator.comparingInt(PeakBean::getScore).thenComparingLong(PeakBean::getPower).reversed());
        match(queue, 0, true);
        match(queue, 0, false);

        logger.info("匹配结束,剩余未匹配数量 len={}", Manager.peakManager.getMatch().size());
    }

    boolean match(PeakBean first, List<PeakBean> commonList, boolean matchLast, int offset) {
        if (commonList.isEmpty() || Math.abs(offset) > MaxStage * MaxStage) {
            return false;
        }
        for (int i = 1; i < commonList.size(); i++) {
            PeakBean next = commonList.get(i);
            if (matchLast && first.getLastMatchId() == next.getRoleId() && first.getRoleId() == next.getLastMatchId()) {
                continue;
            }
            if (first.getRankId() + offset == next.getRankId()) {
                commonList.remove(next);
                createRoom(first, next);
                return true;
            }
        }
        return match(first, commonList, matchLast, offset == 0 ? 0 : offset < 0 ? offset - 1 : offset + 1);
    }

    void match(List<PeakBean> list, int start, boolean matchLast) {
        if (list.size() - start < 2) {
            return;
        }
        boolean match = false;
        PeakBean first = list.get(start);
        for (int i = start + 1; i < list.size(); i++) {
            PeakBean next = list.get(i);
            if (matchLast && first.getLastMatchId() == next.getRoleId() && first.getRoleId() == next.getLastMatchId()) {
                continue;
            }
            list.remove(first);
            list.remove(next);
            match = true;
            createRoom(first, next);
            break;
        }
        match(list, match ? start : start + 1, matchLast);
    }

    void createRoom(PeakBean red, PeakBean blue) {
        Manager.peakManager.getMatch().remove(red.getRoleId());
        Manager.peakManager.getMatch().remove(blue.getRoleId());
        Player rPlayer = Manager.playerManager.getPlayer(red.getRoleId());
        Player bPlayer = Manager.playerManager.getPlayer(blue.getRoleId());
        logger.info("【巅峰竞技场】匹配成功 red={} blue={} ", red, blue);

        MapObject map = Manager.mapManager.createCopyMap(CloneId, 1, red.getRoleId(), red, blue);
        red.setFight(true);
        blue.setFight(true);
        //进入地图
        Manager.mapManager.changeMap(rPlayer, map.getId(), map.getBriths().get(0), false);
        Manager.mapManager.changeMap(bPlayer, map.getId(), map.getBriths().get(1), false);
    }

    /**
     * 新增积分
     */
    @Override
    public void cloneOver(PeakBean win, int winScore, PeakBean loser, int loserScore) {
        long time = TimeUtils.Time();
        win.setScore(win.getScore() + winScore);
        win.setTime(time);
        win.setTimes(win.getTimes() + 1);
        win.setDayTimes(win.getDayTimes() + 1);
        //标记连胜连负
        win.setWinLian(win.getWinLian() + 1);
        win.setLoserLian(0);
        win.setLastMatchId(loser.getRoleId());
        win.setFight(false);

        loser.setScore(loser.getScore() + loserScore);
        loser.setTime(time);
        loser.setTimes(loser.getTimes() + 1);
        loser.setDayTimes(loser.getDayTimes() + 1);
        //标记连胜连负
        loser.setLoserLian(loser.getLoserLian() + 1);
        loser.setWinLian(0);
        loser.setLastMatchId(win.getRoleId());
        loser.setFight(false);

        for (int i = Math.min(win.getRankId(), loser.getRankId()); i <= CfgManager.getCfg_PeakBattleStage_Container().size(); i++) {
            int index = Math.max(i, 1);
            Cfg_PeakBattleStage_Bean bean = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(index);
            if (win.getScore() >= bean.getIntegral()) {
                win.setRankId(bean.getId());
            }
            if (loser.getScore() >= bean.getIntegral()) {
                loser.setRankId(bean.getId());
            }
        }

        Manager.saveThreadManager.getOtherServerSave().deal(win, DbSqlName.PeekUpdate, SaveServer.UPDATE);
        Manager.saveThreadManager.getOtherServerSave().deal(loser, DbSqlName.PeekUpdate, SaveServer.UPDATE);

        List<PeakBean> ranks = Manager.peakManager.getRanks();
        ranks.sort(Comparator.comparingInt(PeakBean::getScore)
                .thenComparingLong(PeakBean::getPower)
                .thenComparingLong(PeakBean::getTime).reversed());
    }

    /**
     * 添加积分
     *
     * @param bean
     * @param score
     */
    @Override
    public void cloneOver(PeakBean bean, int score) {
        long time = TimeUtils.Time();
        bean.setScore(bean.getScore() + score);
        bean.setTime(time);

        for (int i = bean.getRankId(); i <= CfgManager.getCfg_PeakBattleStage_Container().size(); i++) {
            int index = Math.max(i, 1);
            Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(index);
            if (bean.getScore() >= config.getIntegral()) {
                bean.setRankId(config.getId());
            }
        }

        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.UPDATE);
        List<PeakBean> ranks = Manager.peakManager.getRanks();
        ranks.sort(Comparator.comparingInt(PeakBean::getScore)
                .thenComparingLong(PeakBean::getPower)
                .thenComparingLong(PeakBean::getTime).reversed());
    }

    /**
     * 启动服务器加载本服 巅峰竞技
     */
    @Override
    public void loadAll() {
        List<PeakBean> beans = peakDao.selectAll(null);
        for (PeakBean peak : beans) {
            Manager.peakManager.getPeaks().put(peak.getRoleId(), peak);
        }
        Manager.peakManager.getRanks().addAll(beans);
        logger.info("加载巅峰竞技场数据 len={} rank={}", beans.size(), Manager.peakManager.getRanks().size());
    }

    /**
     * 发放未领取次数奖励
     *
     * @param roleId
     * @param timesList
     */
    void sendTimesMail(long roleId, List<Integer> timesList) {
        if (timesList.isEmpty()) {
            return;
        }
        List<Item> items = new ArrayList<>();
        for (int times : timesList) {
            Cfg_PeakBattleJoinReward_Bean config = CfgManager.getCfg_PeakBattleJoinReward_Container().getValueByKey(times);
            items.addAll(Item.createItems(config.getReward()));
        }

        Manager.mailManager.sendMailToPlayer(
                roleId,
                MailType.SysCommonRewardMail,
                MessageString.System,
                MessageString.Peak_Mail_Title,
                MessageString.Peak_Times_Mail,
                items, ItemChangeReason.PeekPkRewardGet);
    }

    /**
     * 发放未领取段位奖励
     *
     * @param roleId
     * @param stageList
     */
    void sendStageMail(long roleId, List<Integer> stageList) {
        if (stageList.isEmpty()) {
            return;
        }

        for (int stageId : stageList) {
            Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(stageId);
            List<Item> items = Item.createItems(config.getStageReward());
            Manager.mailManager.sendMailToPlayer(
                    roleId,
                    MailType.SysCommonRewardMail,
                    MessageString.System,
                    MessageString.Peak_Mail_Title,
                    MessageString.Peak_Stage_Mail,
                    items, ItemChangeReason.PeekPkRewardGet);
        }
    }

    /**
     * 0点刷新
     */
    @Override
    public void zeroClockDeal() {

        if (Manager.countManager.getServerVariant(VariantType.PeakTimesRewardMail) == 0) {
            Manager.countManager.setServerVariant(VariantType.PeakTimesRewardMail, 1);
            //每日凌晨邮件结算未领取场次奖励
            for (PeakBean bean : Manager.peakManager.getPeaks().values()) {
                List<Integer> times = new ArrayList<>();
                for (Cfg_PeakBattleJoinReward_Bean config : CfgManager.getCfg_PeakBattleJoinReward_Container().getValuees()) {
                    if (config.getJoinTimes() > bean.getDayTimes()) {
                        continue;
                    }
                    if (bean.checkTimesRewardState(config.getId())) {
                        continue;
                    }
                    times.add(config.getId());
                }
                bean.setDayTimes(0);
                bean.setTimesReward(0);
                Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.PeekUpdate, SaveServer.UPDATE);
                sendTimesMail(bean.getRoleId(), times);
            }
            logger.info("【巅峰竞技场】 场次奖励结算。。。Time={}", TimeUtils.format2string(TimeUtils.Time()));
        }

        if (Manager.countManager.getServerVariant(VariantType.PeakRankCalc) == 0) {
            Manager.countManager.setServerVariant(VariantType.PeakRankCalc, 1);
            logger.info("【巅峰竞技场】 赛季结算。。。Time={}", TimeUtils.format2string(TimeUtils.Time()));

            //FIXME 2020.11.24 策划说排名奖励一期不做，二期开放
//            for (Cfg_PeakBattleRank_Bean offset : CfgManager.getCfg_PeakBattleRank_Container().getValuees()) {
//
//                int start = offset.getStage().get(0);
//                int end = Math.min(offset.getStage().get(1), Manager.peakManager.getRanks().size());
//                if (start > Manager.peakManager.getRanks().size()) {
//                    break;
//                }
//                List<Item> items = Item.createItems(offset.getRewards());
//                List<PeakBean> ranks = Manager.peakManager.getRanks().subList(start - 1, end);
//                for (PeakBean bean : ranks) {
//                    if (bean.getTimes() < Global.PeakBattle_RankJionTime) {
//                        continue;
//                    }
//                    String mailContent = MailManager.linkContext(MessageString.Peak_Mail, ServerParamUtil.peakSeason, start++);
//                    Manager.mailManager.sendMailToPlayer(
//                            bean.getRoleId(),
//                            MailType.SysCommonRewardMail,
//                            MessageString.System,
//                            MessageString.Peak_Mail_Title,
//                            mailContent,
//                            items);
//                }
//            }

            //未领段位奖励
            List<Integer> limitIds = new ArrayList<>();
            for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
                if (config.getLimitTimes() > 0) {
                    limitIds.add(config.getId());
                }
            }
            HashMap<Long, List<Integer>> playerStageRewards = new HashMap<>();
            Manager.peakManager.getPeaks().forEach((i, o) -> playerStageRewards.put(o.getRoleId(), new ArrayList<>()));

            for (PeakBean bean : Manager.peakManager.getPeaks().values()) {
                List<Integer> list = playerStageRewards.get(bean.getRoleId());
                for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
                    if (config.getId() > bean.getRankId() || limitIds.contains(config.getId()) || bean.checkStageRewardState(config.getId())) {
                        continue;
                    }
                    list.add(config.getId());
                }
            }
            for (int limit : limitIds) {
                Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(limit);
                int remain = config.getLimitTimes() - Manager.countManager.getServerCount(BaseCountType.PeakWeekStageBox, limit);
                if (remain <= 0) {
                    continue;
                }
                List<PeakBean> beans = Utils.find(Manager.peakManager.getPeaks().values(), o -> o.getRankId() >= limit && !o.checkStageRewardState(limit));
                if (beans.isEmpty()) {
                    continue;
                }
                beans.sort(Comparator.comparingLong(PeakBean::getPower).reversed());
                for (int i = 0; i < remain && i < beans.size(); i++) {
                    PeakBean bean = beans.get(i);
                    List<Integer> list = playerStageRewards.get(bean.getRoleId());
                    list.add(limit);
                }
            }

            playerStageRewards.forEach(this::sendStageMail);

            for (int limit : limitIds) {
                Manager.countManager.setServerCount(BaseCountType.PeakWeekStageBox, Count.RefreshType.CountType_Forever, limit, 0);
            }
            ServerParamUtil.peakSeason += 1;
            ServerParamUtil.savePeakSeason();
            peakDao.clearAll();
            Manager.peakManager.getPeaks().clear();
            Manager.peakManager.getRanks().clear();
            logger.info("【巅峰竞技场】 赛季结算完毕。。。");
        }
    }

    /**
     * 进入等待地图
     *
     * @param player
     */
    @Override
    public void reqEnterWaitScene(Player player) {

        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }
        MapObject waitMap = getWaitMap();
        if (waitMap == null) {
            sendCancelMatch(player);
            //TODO 活动未开启
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Daily_Not_In_Time);
            return;
        }
        if (waitMap.getId() != player.gainMapId()) {
            Manager.mapManager.changeMap(player, waitMap.getId(), null, false);
        }

    }

    @Override
    public int getZoneModelId() {
        return CloneId;
    }

    /**
     * 跨服巅峰竞技是否开启
     *
     * @return
     */
    boolean isCrossPeakOpen() {
//        int openDay = TimeUtils.getOpenServerDay();
//        if (openDay < Global.PeakBattle_OpenDay) {
//            return false;
//        }
//        if (Manager.peakManager.getPeaks().isEmpty()) {
//            return true;
//        }
        return true;
    }
}
