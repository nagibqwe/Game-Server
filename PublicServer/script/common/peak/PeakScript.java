package common.peak;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.count.structs.CountBase;
import com.game.count.structs.CountReset;
import com.game.count.structs.CountVariant;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.PeakBean;
import com.game.db.dao.PeakDao;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.peak.script.IPeak;
import com.game.peak.timer.PeakSaveEvent;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.structs.ServerType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.CrossServerMessage;
import game.message.PeakMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Desc TODO
 * @Date 2020/11/16 17:37
 * @Auth ZUncle
 */
public class PeakScript implements IPeak {

    final Logger logger = LogManager.getLogger(PeakScript.class);
    final int MaxStage = 5; //最高段位
    final PeakDao dao = new PeakDao(); //DAO

    /**
     * 发送取消匹配
     */
    void sendCancelMatch(ChannelHandlerContext context, long roleId) {
        if (context == null) {
            return;
        }
        PeakMessage.ResCancelPeakMatch.Builder message = PeakMessage.ResCancelPeakMatch.newBuilder();
        MessageUtils.send_to_player(context, roleId, PeakMessage.ResCancelPeakMatch.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    PeakMessage.PeakRankRole.Builder pack(PeakBean bean) {
        PeakMessage.PeakRankRole.Builder builder = PeakMessage.PeakRankRole.newBuilder();
        builder.setRoleId(bean.getRoleId());
        builder.setName(bean.getName());
        builder.setServerId(bean.getServerId());
        builder.setScore(bean.getScore());
        builder.setStageId(bean.getRankId());
        builder.setPower(bean.getPower());
        return builder;
    }


    /**
     * 取消匹配
     *
     * @param mess
     */
    @Override
    public void G2PCancelPeakMatch(ChannelHandlerContext context, PeakMessage.G2PCancelPeakMatch mess) {
        PeakBean peak = Manager.peakManager.getPeaks().get(mess.getRoleId());
        if (peak == null || peak.isFight()) {
            return;
        }
        if (Manager.peakManager.getMatch() == null) {
            return;
        }
        PeakBean m = Manager.peakManager.getMatch().remove(peak.getRoleId());
        if (m != null) {
            sendCancelMatch(context, mess.getRoleId());
            logger.info("巅峰竞技.取消匹配 player={}", peak);
        }
    }

    PeakBean init(PeakMessage.PeakCrossRole role) {
        PeakBean peak = new PeakBean();
        peak.setRoleId(role.getRoleId());
        peak.setName(role.getName());
        peak.setPlatform(role.getPlatform());
        peak.setServerId(role.getServerId());
        peak.setPower(role.getPower());
        peak.setRankId(1);
        Manager.peakManager.getPeaks().put(peak.getRoleId(), peak);
        Manager.peakManager.getRanks().add(peak);
        Manager.peakManager.addCommand(new PeakSaveEvent(peak.getRoleId()));
        return peak;
    }

    /**
     * 匹配
     *
     * @param mess
     */
    @Override
    public void G2PEnterPeakMatch(ChannelHandlerContext context, PeakMessage.G2PEnterPeakMatch mess) {
        PeakMessage.PeakCrossRole role = mess.getRole();
        if (Manager.peakManager.getMatch() == null) {
            sendCancelMatch(context, role.getRoleId());
            //TODO 活动未开启
            MessageUtils.notify_player(context, role.getRoleId(), MessageString.Daily_Not_In_Time);
            return;
        }

        PeakBean peak = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (peak == null) {
            peak = init(role);
        }
        if (peak.isFight()) {
            logger.info("玩家已经在战斗中 role={}", role);
            return;
        }
        peak.setName(role.getName());
        peak.setPower(role.getPower());
        peak.setServerId(role.getServerId());
        int delay = RandomUtils.random(Global.PeakBattle_MatchServerTime.get(0, 10), Global.PeakBattle_MatchServerTime.get(1, 60)) * 1000;
        peak.setDelay(TimeUtils.Time() + delay);
        Manager.peakManager.getMatch().put(peak.getRoleId(), peak);

        PeakMessage.ResPeakMatchRes.Builder message = PeakMessage.ResPeakMatchRes.newBuilder();
        MessageUtils.send_to_player(context, role.getRoleId(), PeakMessage.ResPeakMatchRes.MsgID.eMsgID_VALUE, message.build().toByteArray());
        Manager.peakManager.addCommand(new PeakSaveEvent(peak.getRoleId()));
        logger.info("巅峰竞技.匹配 player={}", peak);
    }

    /**
     * 获取巅峰数据
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PPeakInfo(ChannelHandlerContext context, PeakMessage.G2PPeakInfo mess) {
        PeakMessage.PeakCrossRole role = mess.getRole();
        PeakBean peak = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (peak == null) {
            peak = init(role);
        }

        PeakMessage.ResPeakInfo.Builder message = PeakMessage.ResPeakInfo.newBuilder();
        message.setAll(mess.getAll());
        message.setWin(mess.getWin());
        message.setDayExp(mess.getDayExp());
        message.setDayPkCount(peak.getDayTimes());

        for (Cfg_PeakBattleJoinReward_Bean bean : CfgManager.getCfg_PeakBattleJoinReward_Container().getValuees()) {
            if (peak.checkTimesRewardState(bean.getId())) {
                message.addDayBoxIds(bean.getJoinTimes());
            }
        }
        MessageUtils.send_to_player(context, role.getRoleId(), PeakMessage.ResPeakInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

//        logger.info("获取巅峰竞技 段位信息 role={}", role);
    }

    /**
     * 获取排名信息
     *
     * @param mess
     */
    @Override
    public void G2PPeakRankInfo(ChannelHandlerContext context, PeakMessage.G2PPeakRankInfo mess) {
        PeakMessage.PeakCrossRole role = mess.getRole();
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
            if (bean.getRoleId() == role.getRoleId()) {
                selfOrder = i + 1;
            }
        }
        PeakBean peak = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (peak == null) {
            peak = init(role);
        }
        PeakMessage.PeakRankRole.Builder selfRank = pack(peak);
        selfRank.setOrder(selfOrder);
        message.setSelfRank(selfRank);

        MessageUtils.send_to_player(context, role.getRoleId(), PeakMessage.ResPeakRankList.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("请求巅峰竞技排名数据 player={}", mess);
    }

    /**
     * 获取段位信息
     *
     * @param mess
     */
    @Override
    public void G2PPeakStageInfo(ChannelHandlerContext context, PeakMessage.G2PPeakStageInfo mess) {

        PeakMessage.PeakCrossRole role = mess.getRole();

        PeakBean peak = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (peak == null) {
            peak = init(role);
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
            int use = (int) Manager.countManager.getServerVariant(CountBase.PeakWeekStageBox, config.getId());
            PeakMessage.PeakStageReward.Builder remain = PeakMessage.PeakStageReward.newBuilder();
            remain.setStateId(config.getId());
            remain.setCount(Math.max(0, config.getLimitTimes() - use));
            message.addRemainReward(remain);
        }
        MessageUtils.send_to_player(context, role.getRoleId(), PeakMessage.ResPeakStageInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("获取巅峰竞技 段位信息 role={}", role);
    }

    /**
     * 领取巅峰竞技段位奖励
     *
     * @param mess
     */
    @Override
    public void G2PPeakStageReward(ChannelHandlerContext context, PeakMessage.G2PPeakStageReward mess) {
        Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(mess.getStageId());
        if (config == null) {
            return;
        }

        PeakMessage.PeakCrossRole role = mess.getRole();

        PeakBean bean = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (bean == null) {
            bean = init(role);
        }
        if (bean.getRankId() < mess.getStageId() || bean.checkStageRewardState(config.getId())) {
            sendStageResult(context, role.getRoleId(), config.getId(), false);
            return;
        }
        //TODO 检测奖励剩余份数
        if (config.getLimitTimes() > 0) {
            long value = Manager.countManager.getServerVariant(CountBase.PeakWeekStageBox, config.getId());
            if (value >= config.getLimitTimes()) {
                sendStageResult(context, bean.getRoleId(), config.getId(), false);
                return;
            }
            Manager.countManager.setServerVariant(CountBase.PeakWeekStageBox, CountReset.Forever, config.getId(), value + 1);
        }
        bean.signStageRewardState(config.getId(), true);
        Manager.peakManager.addCommand(new PeakSaveEvent(bean.getRoleId()));

        PeakMessage.RewardInfo.Builder info = PeakMessage.RewardInfo.newBuilder();
        info.setRoleId(bean.getRoleId());
        info.addId(config.getId());

        send2GameServerStageReward(context, false, info);

        sendStageResult(context, bean.getRoleId(), config.getId(), true);
    }

    void sendStageResult(ChannelHandlerContext context, long roleId, int stageId, boolean success) {

        Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(stageId);
        long remain = config.getLimitTimes() <= 0 ? -1 : config.getLimitTimes() - Manager.countManager.getServerVariant(CountBase.PeakWeekStageBox, config.getId());

        PeakMessage.ResPeakStageResult.Builder message = PeakMessage.ResPeakStageResult.newBuilder();
        message.setStageId(stageId);
        message.setState(success);
        message.setRemain((int) remain);
        MessageUtils.send_to_player(context, roleId, PeakMessage.ResPeakStageResult.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    void send2GameServerStageReward(ChannelHandlerContext context, boolean mail, PeakMessage.RewardInfo.Builder... rewards) {
        PeakMessage.P2GPeakStageReward.Builder message = PeakMessage.P2GPeakStageReward.newBuilder();
        message.setIsMail(mail);
        for (PeakMessage.RewardInfo.Builder info : rewards) {
            message.addInfo(info);
        }
        MessageUtils.send_to_game(context, PeakMessage.P2GPeakStageReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 领取巅峰竞技场次奖励
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PPeakTimesReward(ChannelHandlerContext context, PeakMessage.G2PPeakTimesReward mess) {

        Cfg_PeakBattleJoinReward_Bean config = CfgManager.getCfg_PeakBattleJoinReward_Container().getValueByKey(mess.getTimes());
        if (config == null) {
            return;
        }
        PeakMessage.PeakCrossRole role = mess.getRole();

        PeakBean bean = Manager.peakManager.getPeaks().get(role.getRoleId());
        if (bean == null) {
            bean = init(role);
        }
        if (bean.getDayTimes() < config.getJoinTimes() || bean.checkTimesRewardState(config.getId())) {
            return;
        }
        bean.signTimesRewardState(config.getId(), true);
        Manager.peakManager.addCommand(new PeakSaveEvent(bean.getRoleId()));

        PeakMessage.RewardInfo.Builder info = PeakMessage.RewardInfo.newBuilder();
        info.setRoleId(bean.getRoleId());
        info.addId(config.getId());

        send2GameServerTimesReward(context, false, info);
    }

    void send2GameServerTimesReward(ChannelHandlerContext context, boolean mail, PeakMessage.RewardInfo.Builder... rewards) {
        PeakMessage.P2GPeakTimesReward.Builder message = PeakMessage.P2GPeakTimesReward.newBuilder();
        message.setIsMail(mail);
        for (PeakMessage.RewardInfo.Builder info : rewards) {
            message.addInfo(info);
        }
        MessageUtils.send_to_game(context, PeakMessage.P2GPeakTimesReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 巅峰竞技挑战结束
     *
     * @param mess
     */
    @Override
    public void F2PPeakCloneResult(PeakMessage.F2PPeakCloneResult mess) {

        PeakBean win = Manager.peakManager.getPeaks().get(mess.getWinId());
        PeakBean loser = Manager.peakManager.getPeaks().get(mess.getLoserId());

        Cfg_PeakBattleStage_Bean winConfig = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(win.getRankId() == 0 ? 1 : win.getRankId());
        Cfg_PeakBattleStage_Bean loserConfig = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(loser.getRankId() == 0 ? 1 : loser.getRankId());

        long time = TimeUtils.Time();
        win.setScore(win.getScore() + winConfig.getWinIntegral());
        win.setTime(time);
        win.setTimes(win.getTimes() + 1);
        win.setDayTimes(win.getDayTimes() + 1);
        //标记连胜连负
        win.setWinLian(win.getWinLian() + 1);
        win.setLoserLian(0);
        win.setLastMatchId(loser.getRoleId());
        win.setFight(false);

        loser.setScore(loser.getScore() + loserConfig.getLoseIntegral());
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

        List<PeakBean> ranks = Manager.peakManager.getRanks();
        ranks.sort(Comparator.comparingInt(PeakBean::getScore)
                .thenComparingLong(PeakBean::getPower)
                .thenComparingLong(PeakBean::getTime).reversed());

        Manager.peakManager.addCommand(new PeakSaveEvent(win.getRoleId()));
        Manager.peakManager.addCommand(new PeakSaveEvent(loser.getRoleId()));

        PeakMessage.P2GPeakCloneResult.Builder loserMessage = PeakMessage.P2GPeakCloneResult.newBuilder();
        loserMessage.setRoleId(loser.getRoleId());
        loserMessage.setIsWin(0);
        loserMessage.setExp(loserConfig.getLoseExp());
        MessageUtils.send_to_game(loser.getPlatform(), loser.getServerId(), PeakMessage.P2GPeakCloneResult.MsgID.eMsgID_VALUE, loserMessage.build().toByteArray());

        PeakMessage.P2GPeakCloneResult.Builder winMessage = PeakMessage.P2GPeakCloneResult.newBuilder();
        winMessage.setRoleId(win.getRoleId());
        winMessage.setIsWin(1);
        winMessage.setExp(winConfig.getWinExp());
        MessageUtils.send_to_game(win.getPlatform(), win.getServerId(), PeakMessage.P2GPeakCloneResult.MsgID.eMsgID_VALUE, winMessage.build().toByteArray());
    }

    /**
     * 活动开启
     */
    @Override
    public void start() {

        //刷新玩家状态
        for (PeakBean bean : Manager.peakManager.getPeaks().values()) {
            bean.setFight(false);
        }
        Manager.peakManager.setMatch(new ConcurrentHashMap<>());
        MainServer.getInstance().addTimerEvent(Manager.peakManager.getTimer());

        logger.info("开启巅峰竞技场活动=====>");
    }

    /**
     * 活动结束
     */
    @Override
    public void close() {
        MainServer.getInstance().removeTimerEvent(Manager.peakManager.getTimer());
        if (Manager.peakManager.getMatch() == null) {
            return;
        }
        for (PeakBean bean : Manager.peakManager.getMatch().values()) {
            ChannelHandlerContext channelHandlerContext = Manager.gameServerManager.GetSession(bean.getPlatform(), bean.getServerId());
            sendCancelMatch(channelHandlerContext, bean.getRoleId());
        }
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

        red.setFight(true);
        blue.setFight(true);

        Cfg_Daily_Bean daily = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.PeakPk);

        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(daily.getCloneID().get(0));

        Manager.peakManager.getMatch().remove(red.getRoleId());
        Manager.peakManager.getMatch().remove(blue.getRoleId());

        TeamPlayerInfo redPlayer = new TeamPlayerInfo();
        redPlayer.setServerId(red.getServerId());
        redPlayer.setRoleId(red.getRoleId());
        redPlayer.setName(red.getName());
        redPlayer.setReady(true);

        TeamPlayerInfo bluePlayer = new TeamPlayerInfo();
        bluePlayer.setServerId(blue.getServerId());
        bluePlayer.setRoleId(blue.getRoleId());
        bluePlayer.setName(blue.getName());
        bluePlayer.setReady(true);

        ZoneTeam redTeam = new ZoneTeam();
        redTeam.setPlat(red.getPlatform());
        redTeam.setsId(red.getServerId());
        redTeam.setPow(red.getPower());
        redTeam.getPlist().put(redPlayer.getRoleId(), redPlayer);

        ZoneTeam blueTeam = new ZoneTeam();
        blueTeam.setPlat(blue.getPlatform());
        blueTeam.setsId(blue.getServerId());
        blueTeam.setPow(blue.getPower());
        blueTeam.getPlist().put(bluePlayer.getRoleId(), bluePlayer);

        List<ZoneTeam> teams = new ArrayList<>();
        teams.add(redTeam);
        teams.add(blueTeam);
        logger.info("巅峰竞技场匹配成功 red={} blue={} ", red, blue);

        FightRoom room = Manager.fightManager.deal().createFightRoom(clone, teams);
        room.setAllReadyStart(true);
        fightStart(room, redPlayer, bluePlayer);
    }

    public boolean fightStart(FightRoom mine, TeamPlayerInfo red, TeamPlayerInfo blue) {
        //开始分配服务器
        ServerInfo serverInfo = Manager.fightManager.deal().getFightServerId(Math.max(mine.hasPeoples(), mine.getHaveNum()));
        if (serverInfo == null) {
            List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
            logger.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
            return false;
        }
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(mine.getModelId());

        Cfg_Mapsetting_Bean map = CfgManager.getCfg_Mapsetting_Container().getValueByKey(clone.getMapid());

        mine.setServerId(serverInfo.getServerId());
        mine.setpPlat(serverInfo.getPlatName());

        List<TeamPlayerInfo> team = new ArrayList<>();
        team.add(red);
        team.add(blue);

        List<CommonMessage.CrossAttribute> params = new ArrayList<>();
        Map<ChannelHandlerContext, List<CrossFightMessage.roleAtt.Builder>> players = new HashMap<>();

        for (int i = 0; i < team.size(); i++) {
            TeamPlayerInfo player = team.get(i);
            PeakBean bean = Manager.peakManager.getPeaks().get(player.getRoleId());
            CommonMessage.CrossAttribute.Builder param = CommonMessage.CrossAttribute.newBuilder();
            param.setValue(player.getRoleId());
            param.setType(bean.getScore());
            param.setParam1(bean.getRankId());
            params.add(param.build());

            ChannelHandlerContext session = Manager.gameServerManager.GetSession(bean.getPlatform(), bean.getServerId());
            if (session == null) {
                continue;
            }
            ReadArray<Integer> pos = map.getBornPosition().get(i);
            List<CrossFightMessage.roleAtt.Builder> list = players.getOrDefault(session, new ArrayList<>());
            CrossFightMessage.roleAtt.Builder role = CrossFightMessage.roleAtt.newBuilder();
            role.setRoleId(player.getRoleId());
            role.setX(pos.get(0));
            role.setY(pos.get(1));
            list.add(role);
            players.put(session, list);
        }

        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(mine.getFid());
        msg.addAllMapSetList(params);
        msg.setZoneModelId(mine.getModelId());
        msg.setFightServerId(mine.getServerId());
        msg.setMapModelId(mine.getMapmodelId());

        for (ChannelHandlerContext context : players.keySet()) {
            List<CrossFightMessage.roleAtt.Builder> roleIds = players.get(context);
            for (CrossFightMessage.roleAtt.Builder rb : roleIds) {
                msg.addRoleInfo(rb);
            }
            MessageUtils.send_to_game(context, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        //进入战斗状态
        mine.setRstate(FightRoomState.FIGHTING);
        logger.info("巅峰竞技场开始战斗 red={} blue={} ", red, blue);
        return true;
    }

    /**
     * 启动服务器加载本服 巅峰竞技
     */
    @Override
    public void loadAll() {
        List<PeakBean> beans = dao.selectAll(null);
        for (PeakBean peak : beans) {
            Manager.peakManager.getPeaks().put(peak.getRoleId(), peak);
        }
        Manager.peakManager.getRanks().addAll(beans);
        logger.info("加载巅峰竞技场数据 len={} rank={}", beans.size(), Manager.peakManager.getRanks().size());
    }

    /**
     * 0点刷新
     */
    @Override
    public void zeroClockDeal() {

        if (Manager.countManager.getVariant(() -> ServerParamUtil.counts, CountVariant.PeakTimesMail) == 0) {
            Manager.countManager.setVariant(() -> ServerParamUtil.counts, CountVariant.PeakTimesMail, 1);
            ServerParamUtil.saveCounts();
            //每日凌晨邮件结算未领取场次奖励
            HashMap<String, List<PeakMessage.RewardInfo.Builder>> serverRole = new HashMap<>();
            HashMap<String, ChannelHandlerContext> serverSocket = new HashMap<>();

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
                Manager.peakManager.addCommand(new PeakSaveEvent(bean.getRoleId()));

                if (times.isEmpty()) {
                    continue;
                }
                PeakMessage.RewardInfo.Builder info = PeakMessage.RewardInfo.newBuilder();
                info.setRoleId(bean.getRoleId());
                info.addAllId(times);

                String key = Manager.gameServerManager.makeKey(bean.getPlatform(), bean.getServerId());
                serverRole.putIfAbsent(key, new ArrayList<>());

                List<PeakMessage.RewardInfo.Builder> role = serverRole.get(key);
                role.add(info);

                ChannelHandlerContext session = Manager.gameServerManager.GetSession(key);

                if (session != null) {
                    serverSocket.putIfAbsent(key, session);
                } else {
                    logger.info("服务器连接断开 key={}", key);
                }
            }
            serverSocket.forEach((key, socket) -> {
                List<PeakMessage.RewardInfo.Builder> roles = serverRole.get(key);
                send2GameServerTimesReward(socket, true, roles.toArray(new PeakMessage.RewardInfo.Builder[roles.size()]));
            });
            logger.info("【巅峰竞技场】 场次奖励结算。。。Time={}", TimeUtils.format2string(TimeUtils.Time()));

        }


        if (Manager.countManager.getVariant(() -> ServerParamUtil.counts, CountVariant.PeakRankCalc) == 0) {
            Manager.countManager.setVariant(() -> ServerParamUtil.counts, CountVariant.PeakRankCalc, 1);
            ServerParamUtil.saveCounts();
            logger.info("【巅峰竞技场】 赛季结算。。。Time={}", TimeUtils.format2string(TimeUtils.Time()));

            //FIXME 2020.11.24 策划说排名奖励一期不做，二期开放
//            for (Cfg_PeakBattleRank_Bean offset : CfgManager.getCfg_PeakBattleRank_Container().getValuees()) {
//
//                int start = offset.getStage().get(0);
//                int end = Math.min(offset.getStage().get(1), Manager.peakManager.getRanks().size());
//                if (start > Manager.peakManager.getRanks().size()) {
//                    break;
//                }
//                List<CrossServerMessage.dropItemInfo> items = createItems(offset.getRewards());
//                List<PeakBean> ranks = Manager.peakManager.getRanks().subList(start - 1, end);
//                for (PeakBean bean : ranks) {
//                    if (bean.getTimes() < Global.PeakBattle_RankJionTime) {
//                        continue;
//                    }
//                    String mailContent = linkContext(MessageString.Peak_Mail, ServerParamUtil.peakSeason, start++);
//
//                    CrossServerMessage.P2GSendMailReward.Builder message = CrossServerMessage.P2GSendMailReward.newBuilder();
//                    message.setRoleId(bean.getRoleId());
//                    message.setSender(MessageString.System);
//                    message.setTitle(MessageString.Peak_Mail_Title);
//                    message.setContent(mailContent);
//                    message.addAllItems(items);
//                    message.setReason(ItemChangeReason.xxx);
//                    MessageUtils.send_to_game(bean.getPlatform(), bean.getServerId(), CrossServerMessage.P2GSendMailReward.MsgID.eMsgID_VALUE, message.build().toByteArray());
//                }
//            }

            //未领段位奖励
            List<Integer> limitIds = new ArrayList<>();
            for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
                if (config.getLimitTimes() > 0) {
                    limitIds.add(config.getId());
                }
            }
            HashMap<String, HashMap<Long, PeakMessage.RewardInfo.Builder>> serverRole = new HashMap<>();
            HashMap<String, ChannelHandlerContext> serverSocket = new HashMap<>();

            for (PeakBean bean : Manager.peakManager.getPeaks().values()) {
                String key = Manager.gameServerManager.makeKey(bean.getPlatform(), bean.getServerId());
                serverRole.putIfAbsent(key, new HashMap<>());

                List<Integer> list = new ArrayList<>();
                for (Cfg_PeakBattleStage_Bean config : CfgManager.getCfg_PeakBattleStage_Container().getValuees()) {
                    if (config.getId() > bean.getRankId() || limitIds.contains(config.getId()) || bean.checkStageRewardState(config.getId())) {
                        continue;
                    }
                    list.add(config.getId());
                }
                if (list.isEmpty()) {
                    continue;
                }
                PeakMessage.RewardInfo.Builder info = PeakMessage.RewardInfo.newBuilder();
                info.setRoleId(bean.getRoleId());
                info.addAllId(list);

                HashMap<Long, PeakMessage.RewardInfo.Builder> roles = serverRole.get(key);
                roles.put(bean.getRoleId(), info);

                ChannelHandlerContext session = Manager.gameServerManager.GetSession(key);
                if (session != null) {
                    serverSocket.putIfAbsent(key, session);
                } else {
                    logger.info("服务器连接断开 key={}", key);
                }
            }
            for (int limit : limitIds) {
                Cfg_PeakBattleStage_Bean config = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(limit);

                int remain = config.getLimitTimes() - (int) Manager.countManager.getServerVariant(CountBase.PeakWeekStageBox, limit);
                if (remain <= 0) {
                    continue;
                }
                List<PeakBean> beans = Manager.peakManager.getPeaks().values().stream().filter(o -> o.getRankId() >= limit && !o.checkStageRewardState(limit)).collect(Collectors.toList());
                if (beans.isEmpty()) {
                    continue;
                }
                beans.sort(Comparator.comparingLong(PeakBean::getPower).reversed());
                for (int i = 0; i < remain && i < beans.size(); i++) {
                    PeakBean bean = beans.get(i);
                    String key = Manager.gameServerManager.makeKey(bean.getPlatform(), bean.getServerId());
                    HashMap<Long, PeakMessage.RewardInfo.Builder> roles = serverRole.get(key);
                    PeakMessage.RewardInfo.Builder info = roles.get(bean.getRoleId());
                    if (info == null) {
                        info = PeakMessage.RewardInfo.newBuilder();
                        info.setRoleId(bean.getRoleId());
                        roles.put(bean.getRoleId(), info);
                    }
                    info.addId(limit);

                    ChannelHandlerContext session = Manager.gameServerManager.GetSession(key);
                    if (session != null) {
                        serverSocket.putIfAbsent(key, session);
                    } else {
                        logger.info("服务器连接断开 key={}", key);
                    }
                }
            }

            serverSocket.forEach((key, socket) -> {
                HashMap<Long, PeakMessage.RewardInfo.Builder> roles = serverRole.get(key);
                send2GameServerStageReward(socket, true, roles.values().toArray(new PeakMessage.RewardInfo.Builder[roles.size()]));
            });

            for (int limit : limitIds) {
                Manager.countManager.setServerVariant(CountBase.PeakWeekStageBox, CountReset.Forever, limit, 0);
            }

            ServerParamUtil.peakSeason += 1;
            ServerParamUtil.savePeakSeason();
            dao.clearAll();
            Manager.peakManager.getPeaks().clear();
            Manager.peakManager.getRanks().clear();
            logger.info("【巅峰竞技场】 赛季结算完毕。。。");
        }
    }

    List<CrossServerMessage.dropItemInfo> createItems(ReadIntegerArrayEs beans) {
        List<CrossServerMessage.dropItemInfo> items = new ArrayList<>();
        for (ReadArray<Integer> array : beans.getValuees()) {
            CrossServerMessage.dropItemInfo.Builder item = CrossServerMessage.dropItemInfo.newBuilder();
            item.setItemModelId(array.get(0));
            item.setNum(array.get(1));
            item.setIsBind(array.get(2, 1) == 1);
            item.setNotice(false);
        }
        return items;
    }

    public String linkContext(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object str : args) {
            sb.append(str).append("@_@");
        }
        sb.delete(sb.lastIndexOf("@_@"), sb.length());
        return sb.toString();
    }

    /**
     * 保存到数据库
     *
     * @param roleId
     */
    @Override
    public void saveDB(long roleId) {
        PeakBean bean = Manager.peakManager.getPeaks().get(roleId);
        if (bean == null) {
            return;
        }
        dao.update(bean);
    }

    /**
     * 增加积分
     *
     * @param bean
     * @param score
     */
    @Override
    public void addScore(PeakBean bean, int score) {
        if (bean == null) {
            return;
        }
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

        Manager.peakManager.addCommand(new PeakSaveEvent(bean.getRoleId()));

        List<PeakBean> ranks = Manager.peakManager.getRanks();
        ranks.sort(Comparator.comparingInt(PeakBean::getScore)
                .thenComparingLong(PeakBean::getPower)
                .thenComparingLong(PeakBean::getTime).reversed());
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PeakScript;
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
}
