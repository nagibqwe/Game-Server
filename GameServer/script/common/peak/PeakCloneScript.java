package common.peak;

import com.data.*;
import com.data.bean.Cfg_PeakBattleStage_Bean;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.copymap.structs.FightRoomState;
import com.game.db.bean.PeakBean;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.peak.timer.PeakCloneEvent;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.PeakMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/11/5 17:36
 * @Auth ZUncle
 */
public class PeakCloneScript implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(PeakCloneScript.class);
    final int RedCamp = 1;
    final int BlueCamp = 2;
    final int GameOver = 3;     //副本结束标记
    final int CloneTimeOut = 4; //副本时间
    final int ReadyTimes = 5;   //准备状态
    final int RoomId = 6;       //跨服房间ID
    final int Ready = 7;        //开始准备

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param args
     */
    @Override
    public void onCreate(MapObject mapObject, Object... args) {
        mapObject.setAutoRemove(false);
        PeakBean red;
        PeakBean blue;
        if (args[0] instanceof Long) {
            mapObject.getParams().put(RoomId, args[0]);
            List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) args[1];
            CommonMessage.CrossAttribute redInfo = crossList.get(0);
            red = new PeakBean();
            red.setRoleId(redInfo.getValue());
            red.setScore(redInfo.getType());
            red.setRankId(redInfo.getParam1());

            CommonMessage.CrossAttribute blueInfo = crossList.get(1);
            blue = new PeakBean();
            blue.setRoleId(blueInfo.getValue());
            blue.setScore(blueInfo.getType());
            blue.setRankId(blueInfo.getParam1());

        } else {
            red = (PeakBean) args[0];
            blue = (PeakBean) args[1];
        }

        mapObject.getParams().put(RedCamp, red);
        mapObject.getParams().put(BlueCamp, blue);
        mapObject.getParams().put(GameOver, false);
        mapObject.getParams().put(ReadyTimes, 0);
        mapObject.getParams().put(Ready, false);

        long copyTime = Global.PeakBattle_BattleTime * 1000L;
        mapObject.getParams().put(CloneTimeOut, TimeUtils.Time() + copyTime);

        mapObject.addMapOnceScriptEventTimer(getId(), "timeOut", copyTime);
        mapObject.addMapOnceScriptEventTimer(getId(), "readyOut", 10 * 1000);
        mapObject.addMapLoopScriptEventTimer(getId(), "readyGo", -1, 0, 1000);

        logger.info("巅峰竞技场 map={}", mapObject);
    }

    /**
     * 是否满足进入条件
     * <p>
     * 若不满足，实现脚本给出提示或错误日志
     *
     * @param player
     * @param model  副本zoneId
     * @param level
     * @return 是否满足条件
     */
    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    /**
     * 进入副本地图接口
     *  @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        if (map.getPlayers().size() >= 2) {
            map.getParams().put(Ready, true);
        }

        int ready = map.getParam(ReadyTimes);
        if (ready >= Global.PeakBattle_WaitTime) {
            sendBattleInfo(map, ready);
            PeakBean red = map.getParam(RedCamp);
            if (player.getId() == red.getRoleId()) {
                player.setCamp(RedCamp, true);
            } else {
                player.setCamp(BlueCamp, true);
            }
            Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateTeam, true);//阵营模式
        }
        if (GameServer.getInstance().IsFightServer()) {
            Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.PeakJJC.type());
        } else {
            Manager.retrieveResManager.getScript().onF2GResourceFindChange(player.getId(), RetrieveType.PeakJJC.type());
        }
        logger.info("进入巅峰竞技场 map={} player={}", map.getId(), player);

    }

    /**
     * 离开副本地图接口
     *
     * @param player
     * @param map
     * @param isQuit
     */
    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

        logger.info("退出巅峰竞技场 map={} player={}", map.getId(), player);

        PeakBean red = map.getParam(RedCamp);
        PeakBean blue = map.getParam(BlueCamp);

        if (player.getId() == red.getRoleId()) {
            finish(map, blue, red);
        } else {
            finish(map, red, blue);
        }
    }

    /**
     * 伤害接口
     *
     * @param mapObject
     * @param monster
     * @param damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    /**
     * 怪物死亡接口
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    /**
     * 怪物死亡后
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 玩家死亡接口
     *
     * @param map
     * @param attacker
     * @param player
     */
    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        PeakBean red = map.getParam(RedCamp);
        PeakBean blue = map.getParam(BlueCamp);

        if (attacker.getId() == red.getRoleId()) {
            finish(map, red, blue);
        } else {
            finish(map, blue, red);
        }
    }

    /**
     * 定时执行的函数
     *
     * @param map
     * @param method
     * @param params
     */
    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "readyOut":
                readyOut(map);
                break;
            case "timeOut":
                timeOut(map);
                break;
            case "readyGo":
                readyGo(map);
            default:
        }
    }

    private void readyOut(MapObject map) {
        boolean ready = map.getParam(Ready);
        if (ready){
            return;
        }
        timeOut(map);
    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

    }

    void sendBattleInfo(MapObject map, int ready) {
        long cloneTime = map.getParam(CloneTimeOut);
        int time = (int) ((cloneTime - TimeUtils.Time()) / 1000);
        PeakMessage.ResPeakPkTimeInfo.Builder message = PeakMessage.ResPeakPkTimeInfo.newBuilder();
        message.setTime(time);
        message.setState(ready == 0 ? 0 : 1);
        for (Player player : map.getPlayers().values()) {
            MessageUtils.send_to_player(player, PeakMessage.ResPeakPkTimeInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
//        logger.info("副本倒计时 time={}",time);
    }

    void readyGo(MapObject map) {

        boolean br = map.getParam(Ready);
        if (!br) {
            return;
        }

        int times = map.getParam(ReadyTimes);
        map.getParams().put(ReadyTimes, times + 1);

//        logger.info("巅峰竞技场 Ready={}", ready);

        if (times == 0) {
            sendBattleInfo(map, times);
            logger.info("巅峰竞技场 Ready map={}", map);
            return;
        }
        if (times + 1 == Global.PeakBattle_WaitTime) {
            PeakBean rPeak = map.getParam(RedCamp);
            PeakBean bPeak = map.getParam(BlueCamp);
            Player red = map.getPlayer(rPeak.getRoleId());
            Player blue = map.getPlayer(bPeak.getRoleId());
            Manager.playerManager.manager().onUpdatePkState(red, PlayerDefine.PkStateTeam, true);//阵营模式
            Manager.playerManager.manager().onUpdatePkState(blue, PlayerDefine.PkStateTeam, true);//阵营模式

            red.setCamp(RedCamp, true);
            blue.setCamp(BlueCamp, true);

            sendBattleInfo(map, times);

            if (map.getParams().containsKey(RoomId)) {
                Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTING);
            }
            logger.info("巅峰竞技场 开始战斗 map={}", map);
        }
    }

    void timeOut(MapObject map) {
        PeakBean red = map.getParam(RedCamp);
        Player redPlayer = Manager.playerManager.getPlayersCache().get(red.getRoleId());

        PeakBean blue = map.getParam(BlueCamp);
        Player bluePlayer = Manager.playerManager.getPlayersCache().get(blue.getRoleId());
        if (redPlayer == null || bluePlayer == null) {
            return;
        }
        /**
         * 胜利：一方将另外一方打死、或副本结束时剩余血量百分比较多的一方获胜；剩余血量百分比相同的，战力高的一方获胜；战力也相同则等级高的获胜；等级也相同则随机一方获胜（无平局情况）。
         */
        List<Player> ranks = new ArrayList<>();
        ranks.add(redPlayer);
        ranks.add(bluePlayer);
        ranks.sort((r, b) -> {
            long rr = r.getCurHp() / r.getAttribute().MaxHP();
            long bb = b.getCurHp() / b.getAttribute().MaxHP();
            if (rr > bb) {
                return 1;
            }
            if (rr == bb) {
                if (r.getFightPoint() > b.getFightPoint()) {
                    return 1;
                }
                if (r.getFightPoint() == b.getFightPoint()) {
                    if (r.getLevel() > b.getLevel()) {
                        return 1;
                    }
                    return 0;
                }
            }
            return -1;
        });

        if (ranks.get(1).getId() == red.getRoleId()) {
            finish(map, red, blue);
        } else {
            finish(map, blue, red);
        }
    }

    void finish(MapObject map, PeakBean winPeak, PeakBean loserPeak) {
        boolean isOver = map.getParam(GameOver);
        if (isOver) {
            return;
        }

        map.setStop(true);
        map.setAutoRemove(false);
        map.getParams().put(GameOver, true);

        Player winPlayer = Manager.playerManager.getPlayersCache().get(winPeak.getRoleId());
        Player loserPlayer = Manager.playerManager.getPlayersCache().get(loserPeak.getRoleId());

        Manager.playerManager.manager().onUpdatePkState(winPlayer, PlayerDefine.PkStatePeace, true);//切换和平模式
        Manager.playerManager.manager().onUpdatePkState(loserPlayer, PlayerDefine.PkStatePeace, true);//切换和平模式

        logger.info("挑战结束  win={}, loser={}", winPlayer, loserPlayer);

        Cfg_PeakBattleStage_Bean winConfig = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(winPeak.getRankId() == 0 ? 1 : winPeak.getRankId());
        Cfg_PeakBattleStage_Bean loserConfig = CfgManager.getCfg_PeakBattleStage_Container().getValueByKey(loserPeak.getRankId() == 0 ? 1 : loserPeak.getRankId());

        PeakMessage.ResPeakPkGameOverInfo.Builder winMessage = PeakMessage.ResPeakPkGameOverInfo.newBuilder();
        winMessage.setOldScore(winPeak.getScore());
        winMessage.setIncrScore(winConfig.getWinIntegral());
        winMessage.setEnemyName(loserPlayer.getName());
        winMessage.setState(1);

        PeakMessage.ResPeakPkGameOverInfo.Builder loserMessage = PeakMessage.ResPeakPkGameOverInfo.newBuilder();
        loserMessage.setOldScore(loserPeak.getScore());
        loserMessage.setIncrScore(loserConfig.getLoseIntegral());
        loserMessage.setEnemyName(winPlayer.getName());
        loserMessage.setState(0);

        long log = IDConfigUtil.getLogId();
        //前10场结算奖励
        if (winPeak.getDayTimes() < 10) {
            List<Item> items = Item.createItems(winConfig.getWinExtraReward());
            if (!Manager.backpackManager.manager().addItems(winPlayer, items, ItemChangeReason.PeekPkRewardGet, log)) {
                Manager.mailManager.sendMailToPlayer(winPlayer.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.PeekPkRewardGet);
            }
            for (Item item : items) {
                PeakMessage.PeakReward.Builder mItem = PeakMessage.PeakReward.newBuilder();
                mItem.setItemId(item.getItemModelId());
                mItem.setCount(item.getNum());
                winMessage.addShowReward(mItem);
            }
        }
        //前10场结算奖励
        if (loserPeak.getDayTimes() < 10) {
            List<Item> items = Item.createItems(loserConfig.getLoseExtraReward());
            if (!Manager.backpackManager.manager().addItems(loserPlayer, items, ItemChangeReason.PeekPkRewardGet, log)) {
                Manager.mailManager.sendMailToPlayer(loserPlayer.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.PeekPkRewardGet);
            }
            for (Item item : items) {
                PeakMessage.PeakReward.Builder mItem = PeakMessage.PeakReward.newBuilder();
                mItem.setItemId(item.getItemModelId());
                mItem.setCount(item.getNum());
                loserMessage.addShowReward(mItem);
            }
        }

        Manager.currencyManager.manager().addEXP(winPlayer, winConfig.getWinExp(), ItemChangeReason.PeekPkRewardGet, log);
        if (winConfig.getWinExp() > 0) {
            PeakMessage.PeakReward.Builder mItem = PeakMessage.PeakReward.newBuilder();
            mItem.setItemId(ItemCoinType.EXP);
            mItem.setCount(winConfig.getWinExp());
            winMessage.addShowReward(mItem);
        }

        Manager.currencyManager.manager().addEXP(loserPlayer, loserConfig.getWinExp(), ItemChangeReason.PeekPkRewardGet, log);
        if (loserConfig.getLoseExp() > 0) {
            PeakMessage.PeakReward.Builder eItem = PeakMessage.PeakReward.newBuilder();
            eItem.setItemId(ItemCoinType.EXP);
            eItem.setCount(loserConfig.getLoseExp());
            loserMessage.addShowReward(eItem);
        }

        MessageUtils.send_to_player(winPlayer, PeakMessage.ResPeakPkGameOverInfo.MsgID.eMsgID_VALUE, winMessage.build().toByteArray());
        MessageUtils.send_to_player(loserPlayer, PeakMessage.ResPeakPkGameOverInfo.MsgID.eMsgID_VALUE, loserMessage.build().toByteArray());

        //发放积分
        if (map.getParams().containsKey(RoomId)) {
            PeakMessage.F2PPeakCloneResult.Builder message = PeakMessage.F2PPeakCloneResult.newBuilder();
            message.setWinId(winPlayer.getId());
            message.setLoserId(loserPlayer.getId());
            MessageUtils.send_to_public(PeakMessage.F2PPeakCloneResult.MsgID.eMsgID_VALUE, message.build().toByteArray());
            Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTREWARDEND);
        } else {
            Manager.peakManager.addCommand(new PeakCloneEvent(winPeak, winConfig.getWinIntegral(), loserPeak, loserConfig.getLoseIntegral()));
            Manager.peakManager.deal().P2GPeakCloneResult(winPlayer.getId(), 1, winConfig.getWinExp());
            Manager.peakManager.deal().P2GPeakCloneResult(loserPlayer.getId(), 0, loserConfig.getLoseExp());
        }

        if (GameServer.getInstance().IsFightServer()) {
            //增加跨服副本次数
            Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, winPlayer);
        } else {
            Manager.controlManager.operate(winPlayer, FunctionVariable.ArenaTop_Win, 1);
        }
        PeakBean winBean = Manager.peakManager.getPeaks().get(winPlayer.getId());
        PeakBean loserBean = Manager.peakManager.getPeaks().get(loserPlayer.getId());
        Manager.biManager.getScript().biActivity(winPlayer, BIActiityTypeEnum.PeakStageWin, ItemChangeReason.PeekPkRewardGet, winBean == null ? 0 : winBean.getRankId());
        Manager.biManager.getScript().biActivity(loserPlayer, BIActiityTypeEnum.PeakStageLose, ItemChangeReason.PeekPkRewardGet, loserBean == null ? 0 : loserBean.getRankId());
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.PeakCloneScript;
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
