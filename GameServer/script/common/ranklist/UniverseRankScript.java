package common.ranklist;

import com.data.*;
import com.data.bean.Cfg_Daily_Bean;
import com.data.bean.Cfg_FunctionStart_Bean;
import com.data.bean.Cfg_Universe_rank_Bean;
import com.data.struct.ReadArray;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.ranklist.handler.CalTitleFightPowerHandler;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.universe.script.IUniverseRankScript;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.RankListMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 天墟战场排名脚本
 */
public class UniverseRankScript implements IUniverseRankScript {

    private static final Logger log = LogManager.getLogger(UniverseRankScript.class);

    @Override
    public int getId() {
        return ScriptEnum.UniverseRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getRankType() {
        return RankType.UNIVERSE_RANK;
    }

    @Override
    public void onReqUniverseRankPanel(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TerritorialWarCelebrity)) {
            return;
        }

        //活动阶段检查
        int sNum = getCrossServerNum();
        //名人堂关闭
        if (sNum <= 0 || sNum >= 8) {
            RankListMessage.ResUniverseRankPanel.Builder builder = getNullRankData();
            MessageUtils.send_to_player(player, RankListMessage.ResUniverseRankPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            return;
        }

        RankListMessage.ResUniverseRankPanel.Builder builder = getRankData();
//        log.info("=========名人堂排名:"+builder.build().toString());
        MessageUtils.send_to_player(player, RankListMessage.ResUniverseRankPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private RankListMessage.ResUniverseRankPanel.Builder getRankData() {
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(getRankType());
        if (rankMap == null || rankMap.isEmpty()) {
            return getNullRankData();
        }
        RankListMessage.ResUniverseRankPanel.Builder builder = RankListMessage.ResUniverseRankPanel.newBuilder();
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
            rankInfo.setRankData(String.valueOf(rankPlayer.getUniverseFightPower()));
            rankInfo.setRoleId(rankPlayer.getRoleId());
            rankInfo.setLevel(rankPlayer.getLevel());
            rankInfo.setCareer(rankPlayer.getCareer());
            rankInfo.setFightPower(rankPlayer.getFightPower());
            rankInfo.setIsOnline(Manager.playerManager.isOnline(rankPlayer.getRoleId()));
            builder.addRankInfoList(rankInfo);
        }
        builder.setStage(getUniverseStage());
        return builder;
    }

    private RankListMessage.ResUniverseRankPanel.Builder getNullRankData() {
        RankListMessage.ResUniverseRankPanel.Builder builder = RankListMessage.ResUniverseRankPanel.newBuilder();
        RankListMessage.RankInfo.Builder rankInfo = RankListMessage.RankInfo.newBuilder();
        rankInfo.setRank(0);
        rankInfo.setRoleName("");
        rankInfo.setRankData("");
        rankInfo.setRoleId(0);
        rankInfo.setLevel(0);
        rankInfo.setCareer(0);
        rankInfo.setFightPower(0);
        rankInfo.setIsOnline(false);
        builder.addRankInfoList(rankInfo);
        builder.setStage(0);
        return builder;
    }

    /**
     * 名人堂刷新排名
     */
    @Override
    public void sortUniverseRank() {
        if (TimeUtils.getOpenServerDay() < Global.UniverseSeverOpenTime) {
            return;
        }
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        if (dailyBean == null || dailyBean.getCrossMatch().isEmpty()) {
            return;
        }
        int worldLevel = dailyBean.getCrossMatch().get(0).get(1);
        if (ServerParamUtil.worldLv < worldLevel) {
            return;
        }

        //检查等级开放条件
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.LEVEL_RANK);
        if (rankMap == null || rankMap.isEmpty()) {
            return;
        }
        long roleId = rankMap.get(1);
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player != null) {
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TerritorialWarCelebrity)) {
                return;
            }
        } else {
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
            if (rankPlayer == null) {
                return;
            }
            Cfg_FunctionStart_Bean bean = CfgManager.getCfg_FunctionStart_Container().getValueByKey(FunctionStart.TerritorialWarCelebrity);
            if (bean == null) {
                return;
            }
            for (ReadArray<Integer> ppt : bean.getStart_variables().getValuees()) {
                if (ppt.get(0) == FunctionVariable.PlayerLevel) {
                    if (rankPlayer.getLevel() < ppt.get(1)) {
                        return;
                    }
                }
            }
        }

        //检查阶段
        int stage = getUniverseStage();
        if (stage == 0) {
            return;
        }
        sortUniverseRank(stage, 0);
    }

    private void sortUniverseRank(int stage, int oldStage) {
        boolean stageChange = false;
        boolean isFinish = false;
        Cfg_Universe_rank_Bean oldBean = null;
        Cfg_Universe_rank_Bean bean = CfgManager.getCfg_Universe_rank_Container().getValueByKey(stage);
        if (oldStage > 0 && stage > oldStage) {
            oldBean = CfgManager.getCfg_Universe_rank_Container().getValueByKey(oldStage);
            stageChange = true;
            if (stage == 3) {
                bean = oldBean;
                isFinish = true;
            }
        }
        //从战力榜中排除掉上次排名中称号加的战力，未实际移除称号
        Map<Long, Integer> lastRankMap = new HashMap<>();
        ConcurrentHashMap<Integer, Long> universeRankMap = RankListManager.getTempRankMap().get(getRankType());
        if (universeRankMap == null) {
            return;
        }
        for (Map.Entry<Integer, Long> entry : universeRankMap.entrySet()) {
            int top = entry.getKey();
            Player player = Manager.playerManager.getPlayer(entry.getValue());
            if(player == null){
               continue;
            }
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(entry.getValue());
            if (rankPlayer == null) {
                continue;
            }

            rankPlayer.setFightPower(player.getFightPoint());

            lastRankMap.put(rankPlayer.getRoleId(), top);

            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
            long fightPower = rankPlayer.getFightPower();
            for (int i = 0; i < bean.getRank().size(); i++) {
                ReadArray<Integer> array = bean.getRank().get(i);
                //临时称号不加入战力计算
                if (titleList.containsKey(array.get(2))) {
                    fightPower = rankPlayer.getFightPower() - array.get(4);
                }
            }
            rankPlayer.setUniverseFightPower(fightPower);
        }
        universeRankMap.clear();

        //战力榜重新排名取满足条件的前30的玩家进名人堂
        Manager.rankListManager.deal().sortRank(RankType.FIGHT_POWER_RANK);
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.FIGHT_POWER_RANK);
        int rank = 1;
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(entry.getValue());
            if (rankPlayer == null) {
                continue;
            }
            Player player = Manager.playerManager.getPlayer(rankPlayer.getRoleId());
            if (player == null) {
                continue;
            }
            if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TerritorialWarCelebrity)) {
                continue;
            }
            if (rank > Global.BossHomeNeedRank) {
                continue;
            }
            universeRankMap.put(rank++, rankPlayer.getRoleId());
        }

        //名人堂中前30名发送称号奖励
        for (Map.Entry<Integer, Long> entry : universeRankMap.entrySet()) {
            long roleId = entry.getValue();
            int rankIndex = entry.getKey();
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
            Player player = Manager.playerManager.getPlayer(roleId);
            if (player == null) {
                continue;
            }
//            log.info("==============玩家:" + player.getInfo() + " 排名：" + rankIndex);
            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();

            int lastRank = lastRankMap.getOrDefault(roleId, 0);
            boolean change = false;

            //检查排名称号奖励
            for (int i = 0; i < bean.getRank().size(); i++) {
                ReadArray<Integer> array = bean.getRank().get(i);
                if (rankIndex >= array.get(0) && rankIndex <= array.get(1)) {
//                    rankPlayer.setFightPower(rankPlayer.getFightPower() + array.get(4));

                    //阶段结束时，发放上一阶段的永久称号
                    if (stageChange) {
                        ReadArray<Integer> oldArray = oldBean.getRank().get(i);
                        if (!titleList.containsKey(oldArray.get(3))) {
                            Manager.titleManager.deal().useTitleItem(player, array.get(2), 1, ItemChangeReason.UniverseGet);
//                            titleList.put(oldArray.get(3), 0);
                        }
//                        if (player.isOnline()) {
                        Manager.titleManager.deal().onReqWearTitle(player, array.get(2));
//                        }
                        //发邮件通知
                        String content = MessageString.UNIVERSE_RANK_END_TXT + "@_@" + oldArray.get(1);
                        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                                MessageString.UNIVERSE_RANK_END_MAIL, content);
//                        log.info("==============发送永久称号，玩家:" + player.getInfo() + " 排名：" + rankIndex);
                    }

                    //发放临时称号
                    if (!titleList.containsKey(array.get(2))) {
                        Manager.titleManager.deal().useTitleItem(player, array.get(2), 1, ItemChangeReason.UniverseGet);
//                        if (player.isOnline()) {
                        Manager.titleManager.deal().onReqWearTitle(player, array.get(2));
//                        }
//                        titleList.put(array.get(2), 0);
                        change = true;
//                        log.info("==============发送临时称号，玩家:" + player.getInfo() + " 排名：" + rankIndex);
                    }

                    //临时称号不计入战力排行
                    if (titleList.containsKey(array.get(2))) {
                        rankPlayer.setUniverseFightPower(rankPlayer.getFightPower() - array.get(4));
                    }else{
                        rankPlayer.setUniverseFightPower(rankPlayer.getFightPower());
                    }

                    //上次排名不在本次排名内，发邮件通知进入排行榜
                    if (lastRank == 0 || lastRank < array.get(0) || lastRank > array.get(1)) {
                        String content = MessageString.HallFameJoinRankContent + "@_@" + array.get(1);
                        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                                MessageString.HallFameJoinRankTitle, content);
                    }
                } else {//不在指定排名范围内且拥有称号，卸掉
                    if (titleList.containsKey(array.get(2))) {
//                        log.info("========玩家:" + player.getInfo() + " 卸掉称号：" + array.get(2) + "战力:" + rankPlayer.getFightPower() + ",名人堂战力：" + rankPlayer.getUniverseFightPower());
                        titleList.remove(array.get(2));
                        change = true;
//                        if (player.isOnline()) {
                        Manager.titleManager.deal().onReqDownTitle(player, array.get(2));
//                        }
                    }
                }
            }

            //在线玩家同步消息
            if (change) {
//                log.info("==============发送称号信息，玩家:" + player.getInfo() + " 排名：" + rankIndex);
                Manager.titleManager.deal().sendTitleInfo(player);
                MapServer map = GameServer.getInstance().getMServer(player.gainMapId());
                if (map != null) {
                    map.addCommand(new CalTitleFightPowerHandler(player.getId()));
                }
            }

            if (isFinish) {//所有阶段结束通知
                //发邮件通知
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                        MessageString.UNIVERSE_RANK_ENDING_MAIL, MessageString.UNIVERSE_RANK_ENDING_TXT);
            }
        }

        //上次在名人堂内，本次刷新移除的处理
        for (Map.Entry<Long, Integer> entry : lastRankMap.entrySet()) {
            if (universeRankMap.containsKey(entry.getValue())) {
                continue;
            }
            Player player = Manager.playerManager.getPlayer(entry.getKey());
            ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
            for (int i = 0; i < bean.getRank().size(); i++) {
                ReadArray<Integer> array = bean.getRank().get(i);
                if (entry.getKey() >= array.get(0) && entry.getKey() <= array.get(1)) {
//                    log.info("========移除不在本次排名中的玩家称号,universeRankMap:" + universeRankMap.size() + ",lastRankMap:" + lastRankMap);
                    titleList.remove(array.get(2));
                    if (player.isOnline()) {
                        Manager.titleManager.deal().uninstallTitle(player, array.get(2));
                    }
                }
            }
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.HallFameOutRankTitle, MessageString.HallFameOutRankContent);
        }
    }

    /**
     * 根据服务器数量获取当前阶段
     */
    private int getUniverseStage() {
        int sNum = getCrossServerNum();
        //名人堂关闭
        if (sNum <= 0 || sNum >= 8) {
            return 0;
        }
        int stage = getStageByNum(sNum);
        return stage;
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
        return rankPlayer.getUniverseFightPower();
    }

    private int getCrossServerNum() {
        int sNum = 0;//天墟战场匹配的服务器数量，用于检查的跨服阶段
        String str = ServerParamUtil.crossDailyData.get(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        if (str != null) {
            sNum = Integer.parseInt(str);
        }
        return sNum;
    }

    @Override
    public void checkUniverseStage() {
        log.info("检查天墟战场名人堂排名");
        //检查天墟战场是否开启
        if (TimeUtils.getOpenServerDay() < Global.UniverseSeverOpenTime) {
            return;
        }
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        if (bean == null || bean.getCrossMatch().isEmpty()) {
            return;
        }
        int worldLevel = bean.getCrossMatch().get(0).get(1);
        if (ServerParamUtil.worldLv < worldLevel) {
            return;
        }

        //检查活动是否结束
        int num = getCrossServerNum();
        if (num >= 8) {
            return;
        }
        CrossServerMessage.G2PDailyData.Builder msg = CrossServerMessage.G2PDailyData.newBuilder();
        msg.setDailyId(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        MessageUtils.send_to_public(CrossServerMessage.G2PDailyData.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void updateUniverseStage(int num) {
        int historyNum = getCrossServerNum();
        if (historyNum >= 8 || (historyNum == 0 && num == 0)) {//活动关闭
            return;
        }
        int newNum = num;
        if (historyNum > num) {//可能的跨服重启过，以游戏服历史数量为准
            newNum = historyNum;
        } else if (historyNum < num) {//更新天墟战场进度
            ServerParamUtil.crossDailyData.put(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue(), String.valueOf(newNum));
            ServerParamUtil.saveCrossDailyData();
        }

        int oldStage = getStageByNum(historyNum);
        int newStage = 0;
        if (newNum >= 8) {//所有阶段结束
            newStage = 3;
        } else {
            newStage = getStageByNum(newNum);
        }

        //排序
        sortUniverseRank(newStage, oldStage);

        if (newNum >= 8) {//活动结束,通知在线玩家隐藏标签
            RankListManager.getTempRankMap().get(getRankType()).clear();
            //通知在线玩家关闭活动
            RankListMessage.ResUniverseRankPanel.Builder builder = getNullRankData();
            for (Player player : Manager.playerManager.getOnLines()) {
                MessageUtils.send_to_player(player, RankListMessage.ResUniverseRankPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
    }

    private void endUniverseRank() {
        RankListManager.getTempRankMap().get(getRankType()).clear();
        //通知在线玩家关闭活动
        RankListMessage.ResUniverseRankPanel.Builder builder = getNullRankData();
        for (Player player : Manager.playerManager.getOnLines()) {
            MessageUtils.send_to_player(player, RankListMessage.ResUniverseRankPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    private int getStageByNum(int newNum) {
        for (Cfg_Universe_rank_Bean bean : CfgManager.getCfg_Universe_rank_Container().getValuees()) {
            if (newNum < bean.getTime()) {
                return bean.getId();
            }
        }
        return 0;
    }
}
