package common.ranklist;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Hall_Fame_Bean;
import com.data.container.Cfg_Hall_Fame_Container;
import com.data.struct.ReadArray;
import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.ranklist.handler.CalTitleFightPowerHandler;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.script.ITopHallScript;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.structs.AttributeType;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import game.message.RankListMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 名人堂脚本
 */
public class TopHallRankScript implements ITopHallScript {

    private static final Logger log = LogManager.getLogger(TopHallRankScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TopHallRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getRankType() {
        return RankType.TOPHALL_RANK;
    }

    @Override
    public void onReqTopHallRankPanel(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Celebrith)) {
            return;
        }
        RankListMessage.ResHallFamePanel.Builder builder = getRankData();
        MessageUtils.send_to_player(player, RankListMessage.ResHallFamePanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private RankListMessage.ResHallFamePanel.Builder getRankData() {
        RankListMessage.ResHallFamePanel.Builder builder = RankListMessage.ResHallFamePanel.newBuilder();
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.TOPHALL_RANK);
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            long roleId = entry.getValue();
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
            if (rankPlayer == null) {
                log.error("排行数据错误：找不到排行榜玩家【" + roleId + "】");
                continue;
            }
            RankListMessage.RankInfo.Builder rankInfo = RankListMessage.RankInfo.newBuilder();
            rankInfo.setRank(entry.getKey());
            rankInfo.setRoleName(rankPlayer.getRoleName());
            rankInfo.setRankData(String.valueOf(rankPlayer.getTopHallFightPower()));
            rankInfo.setRoleId(rankPlayer.getRoleId());
            rankInfo.setLevel(rankPlayer.getLevel());
            rankInfo.setCareer(rankInfo.getCareer());
            rankInfo.setFightPower(rankPlayer.getFightPower());
            rankInfo.setIsOnline(Manager.playerManager.isOnline(rankPlayer.getRoleId()));
            builder.addRankInfoList(rankInfo);
        }
        builder.setStage(getTopHallStage());
        builder.setEndTime(getCurStageEndTime());
        return builder;
    }

    /**
     * 名人堂刷新排名
     */
    @Override
    public void sortTopHallRank() {
        //所有阶段全部结束
//        int stage = getTopHallStage();
//        if (stage == 0) {
//            return;
//        }
//
//        //从战力榜中排除掉上次排名中称号加的战力，未实际移除称号
//        HashMap<Long, Integer> lastRankMap = new HashMap<>();
//        Cfg_Hall_Fame_Bean bean = CfgManager.getCfg_Hall_Fame_Container().getValueByKey(stage);
//        ConcurrentHashMap<Integer, Long> topHallRankMap = RankListManager.getTempRankMap().get(RankType.TOPHALL_RANK);
//        if (topHallRankMap == null || topHallRankMap.isEmpty()) {
//            return;
//        }
//        for (Map.Entry<Integer, Long> entry : topHallRankMap.entrySet()) {
//            int top = entry.getKey();
//            Player player =  Manager.playerManager.getPlayer(entry.getValue());
//            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(entry.getValue());
//            if (rankPlayer == null) {
//                continue;
//            }
//            if (player.isOnline()) {
//                rankPlayer.setFightPower(player.getFightPoint());
//            }
//            lastRankMap.put(rankPlayer.getRoleId(), top);
//
//            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
//            for (int i = 0; i < bean.getRank().size(); i++) {
//                ReadArray<Integer> array = bean.getRank().get(i);
//                if (titleList.containsKey(array.get(2))) {
//                    rankPlayer.setFightPower(rankPlayer.getFightPower() - array.get(3));
//                }
//            }
//            rankPlayer.setTopHallFightPower(rankPlayer.getFightPower());
//        }
//        topHallRankMap.clear();
//
//        //战力榜重新排名取满足条件的前30的玩家进名人堂
//        Manager.rankListManager.deal().sortRank(RankType.FIGHT_POWER_RANK);
//        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.FIGHT_POWER_RANK);
//        int rank = 1;
//        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
//            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(entry.getValue());
//            if (rankPlayer == null) {
//                continue;
//            }
//            Player player = Manager.playerManager.getPlayer(rankPlayer.getRoleId());
//            if (player == null) {
//                continue;
//            }
//            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Celebrith)) {
//                continue;
//            }
//            if (rank > Global.BossHomeNeedRank) {
//                continue;
//            }
//            topHallRankMap.put(rank++, rankPlayer.getRoleId());
//        }
//
        //名人堂中前30名发送称号奖励
//        for (Map.Entry<Integer, Long> entry : topHallRankMap.entrySet()) {
//            long roleId = entry.getValue();
//            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(entry.getValue());
//            Player player = Manager.playerManager.getPlayer(roleId);
//            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
//
//            int lastRank = lastRankMap.getOrDefault(roleId, 0);
//            boolean change = false;
//
//            //检查排名称号奖励
//            for (int i = 0; i < bean.getRank().size(); i++) {
//                ReadArray<Integer> array = bean.getRank().get(i);
//                if (entry.getKey() >= array.get(0) && entry.getKey() <= array.get(1)) {
//
//                    rankPlayer.setTopHallFightPower(rankPlayer.getFightPower());
//                    rankPlayer.setFightPower(rankPlayer.getFightPower() + array.get(3));
//
//                    if (!titleList.containsKey(array.get(2))) {
//                        titleList.put(array.get(2), 0);
//                        change = true;
//                    }
//
//                    //上次排名不在本次排名内，发邮件通知进入排行榜
//                    if (lastRank == 0 || lastRank < array.get(0) || lastRank > array.get(1)) {
//                        String content = MessageString.HallFameJoinRankContent + "@_@" + array.get(1);
//                        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
//                                MessageString.HallFameJoinRankTitle, content, null);
//                    }
//
//                    if (player.isOnline()) {
//                        Manager.titleManager.deal().onReqWearTitle(player, array.get(2));
//                    }
//                } else {
//                    //不在指定范围内且拥有称号，卸掉
//                    if (titleList.containsKey(array.get(2))) {
//                        titleList.remove(array.get(2));
//                        change = true;
//                        if (player.isOnline()) {
//                            Manager.titleManager.deal().onReqDownTitle(player, array.get(2));
//                        }
//                    }
//                }
//            }
//
//            //在线玩家同步消息
//            if (player.isOnline() && change) {
//                Manager.titleManager.deal().sendTitleInfo(player);
//                MapServer map = GameServer.getInstance().getMServer(player.gainMapId());
//                if (map != null) {
//                    map.addCommand(new CalTitleFightPowerHandler(player.getId()));
//                }
//            }
//        }
//
//        //上次在名人堂内，本次刷新移除的处理
//        for (Map.Entry<Long, Integer> entry : lastRankMap.entrySet()) {
//            if (topHallRankMap.containsKey(entry.getValue())) {
//                continue;
//            }
//            Player player = Manager.playerManager.getPlayer(entry.getKey());
//            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
//            for (int i = 0; i < bean.getRank().size(); i++) {
//                ReadArray<Integer> array = bean.getRank().get(i);
//                if (entry.getKey() >= array.get(0) && entry.getKey() <= array.get(1)) {
//                    titleList.remove(array.get(3));
//                    if (player.isOnline()) {
//                        Manager.titleManager.deal().uninstallTitle(player, array.get(2));
//                    }
//                }
//            }
//            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
//                    MessageString.HallFameOutRankTitle, MessageString.HallFameOutRankContent, null);
//        }
    }

    /**
     * 零点名人堂阶段检查
     */
    @Override
    public void zeroCheckTopHallStage() {
        //阶段是否结束
        boolean stageOver = false;
        int openDay = getCurOpenDay();
        for (Cfg_Hall_Fame_Bean bean : CfgManager.getCfg_Hall_Fame_Container().getValuees()) {
            if (openDay - 1 == bean.getTime()) {
                stageOver = true;
                break;
            }
        }
        if (!stageOver) {
            return;
        }

        //重新排名
        sortTopHallRank();

        //阶段改变全服通知
        RankListMessage.ResHallFamePanel.Builder builder = getRankData();
        MessageUtils.send_to_all_player(RankListMessage.ResHallFamePanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private int getCurOpenDay() {
        try {
            Date open = TimeUtils.getDateByString(ServerConfig.getServerOpenTime());
            long zday = TimeUtils.GetCurTimeInMin(4, open.getTime());
            long sday = TimeUtils.GetCurTimeInMin(4, TimeUtils.Time() - 60000);
            return (int)(sday - zday) + 1;
        } catch (ParseException var6) {
            log.error(var6, var6);
            return 0;
        }
    }

    /**
     * 根据开服时间获取当前阶段
     */
    private int getTopHallStage() {
        int openDay = getCurOpenDay();
        for (Cfg_Hall_Fame_Bean bean : CfgManager.getCfg_Hall_Fame_Container().getValuees()) {
            if (openDay <= bean.getTime()) {
                return bean.getId();
            }
        }
        return 0;
    }

    /**
     * 获取当前阶段结束的剩余时间（秒）
     */
    private int getCurStageEndTime() {
        int openDay = TimeUtils.getOpenServerDay();
        int stageId = getTopHallStage();
        if (stageId == 0) {
            return 0;
        }
        Cfg_Hall_Fame_Bean bean = CfgManager.getCfg_Hall_Fame_Container().getValueByKey(stageId);
        int dayOfSecond = (int) ((TimeUtils.Time() - TimeUtils.getTodayBeginTime()) / 1000) ;
        return (bean.getTime() - openDay + 1) * GlobalType.SECOND_PER_DAY - dayOfSecond;
    }

    @Override
    public List<RankListMessage.RankInfo.Builder> getRankInfo() {
        return new ArrayList<>();
    }

    @Override
    public boolean canRank(RankPlayer rankPlayer) {
        return false;
    }

    @Override
    public int compareRankPlayer(RankPlayer p1, RankPlayer p2) {
        if (getCompareValue(p2) != getCompareValue(p1)) {
            return getCompareValue(p2) > getCompareValue(p1) ? 1 : -1;
        }
        return 0;
    }

    @Override
    public long getCompareValue(RankPlayer rankPlayer) {
        return rankPlayer.getTopHallFightPower();
    }

}
