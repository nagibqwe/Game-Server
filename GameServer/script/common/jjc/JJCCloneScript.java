package common.jjc;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Jjcrobot_Bean;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.dao.JJCDao;
import com.game.jjc.command.JJCSortICommand;
import com.game.jjc.manager.JJCManager;
import com.game.jjc.script.IJJCCloneScript;
import com.game.jjc.structs.JJC;
import com.game.jjc.structs.JJCReport;
import com.game.manager.Manager;
import com.game.map.structs.Area;
import com.game.map.structs.MapObject;
import com.data.MessageString;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.ReliveType;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.JJCMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

/**
 * 竞技场的战场处理
 *
 * @author admin
 */
public class JJCCloneScript implements IJJCCloneScript {

    private static final Logger logger = LogManager.getLogger("JJCCloneScript");
    /**
     * 每场战斗结束标志
     */
    private boolean finishFlag = false;
    /**
     * 每场战斗结束时间
     */
    private Instant finishTime;


    private int waitTime = 4000;//准备时间，策划欧阳帆不配置，喊写死，

    @Override
    public int getId() {
        return ScriptEnum.JJCCloneActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_End, false);
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_Begin, false);
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_RoundTime, 0);
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 1000, 0);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    /**
     * 用于在同一个竞技场实例中，继续战斗，不用退出去再进
     */
    @Override
    public void keepFighting(Player player, MapObject map) {
        MapParam.getJJCParam(map).put(JJCManager.JJC_Begin, false);
        MapParam.getJJCParam(map).put(JJCManager.JJC_End, false);
        onEnterMap(player, map, false);
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        boolean isOver = (boolean) MapParam.getJJCParam(map).get(JJCManager.JJC_End);
        if (isOver) {
            return;
        }
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        boolean isBegin = (boolean) MapParam.getJJCParam(map).get(JJCManager.JJC_Begin);

        if (!isBegin) {

            if (EntityState.Dead.compare(player.getState())) {
                Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, ReliveType.Gm, false, player.gainCurPos());
            }
            player.reset();
            player.onHpChange(player);

            //玩家进入地图完成
            Robot robot = (Robot) MapParam.getJJCParam(map).get(JJCManager.JJC_Robert);
            //机器人工作时间
            robot.BeginWork = TimeUtils.Time() + waitTime;

            Manager.mapManager.manager().onEnterMap(robot);
            //机器人不带宠物
            //if (robot.getPet() != null) {
            //    MapGpsUtil.CopyGPS(robot.getCurGps(), robot.getPet().getCurGps());
            //    Manager.mapManager.manager().onEnterMap(robot.getPet());
            //}

            handleParticipateOnceJJC(player);
            MapParam.getJJCParam(map).put(JJCManager.JJC_Begin, true);
            MapParam.getJJCParam(map).put(JJCManager.JJC_RoundTime, TimeUtils.Time() + Global.JJCOnceTime);
            OnTimeOutClose(map, bean.getExist_time());
            //OnThisRoundOver(map,20000);
        }

        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateTeam, true);//阵营模式

        //int waitTime = (int) (bean.getEnter_time() - (TimeUtils.Time() - map.getCreate()));
        //if (waitTime < 0) {
        //    waitTime = 0;
        //}

        CopyMapMessage.ResCopymapNeedTime.Builder msg = CopyMapMessage.ResCopymapNeedTime.newBuilder();
        msg.setEndTime(Global.JJCOnceTime / 1000);
        msg.setModelId(map.getZoneModelId());
        msg.setWaitEndToStart(waitTime);
        MessageUtils.send_to_player(player, CopyMapMessage.ResCopymapNeedTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.copyMapManager.logic().biInstance(player, map.getZoneModelId(), 0, 1, 0, false);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        boolean isOver = (boolean) MapParam.getJJCParam(map).get(JJCManager.JJC_End);
        if (isOver) {
            return;
        }

        MapParam.getJJCParam(map).put(JJCManager.JJC_End, true);
        for (long robtID : map.getRobots().keySet()) {
            if (Manager.jjcManager.getChallengedLock().containsKey(robtID))
                Manager.jjcManager.getChallengedLock().remove(robtID);
            Manager.mapManager.manager().onQuitMap(map, map.getRobots().get(robtID), true);

        }
        if (Manager.jjcManager.getChallengedLock().containsKey(player.getId()))
            Manager.jjcManager.getChallengedLock().remove(player.getId());
        if (!isQuit) {//主动退出
            Manager.copyMapManager.outZone(player);
        }

        player.reset();
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平
        MapParam.getJJCParam(map).put(JJCManager.JJC_RoundTime, 0);
        MapParam.getJJCParam(map).put(JJCManager.JJC_Player, null);
        map.setStop(true);
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        pet.reset();

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter acktter) {
    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter fighter) {
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter fighter, Player player) {

        boolean isOver = (boolean) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_End);
        if (isOver) {
            return;
        }

        Robot robot = (Robot) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Robert);
        dealFail(mapObject, player);
        dealFinish(mapObject, player, robot);

        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, 2, 0, false);
    }

    @Override
    public void action(MapObject mapObject, String mothed, Object[] params) {
        Player player = (Player) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Player);
        // if(finishFlag) {
        //     long diff = Duration.between(Instant.now(), finishTime).getSeconds();
        //     if(diff > 20L) {
        //         onQuitMap(player,mapObject, true);
        //     }
        // }

        switch (mothed) {
            case "tick":
                tickRound(mapObject, player);
                break;
            case "close":
                onQuitMap(player, mapObject, true);
                break;
            case "dealFail":
                onQuitMap(player, mapObject, true);
                break;
            case "clearRobot":
                Robot robot = (Robot) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Robert);
                robotExitBattle(player, robot, mapObject);
                break;
            default:
                break;
        }
    }

    public void tickRound(MapObject mapObject, Player player) {
        Object obj = MapParam.getJJCParam(mapObject).get(JJCManager.JJC_RoundTime);
        long roundTime = Long.valueOf(String.valueOf(obj));
        if (roundTime <= 0)
            return;
        long now = TimeUtils.Time();
        if (now >= roundTime) {
            dealFail(mapObject, player);
            Robot robot = (Robot) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Robert);
            dealFinish(mapObject, player, robot);
            Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, 3, 0, false);
        }
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter fighter) {
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    //挑战超时
    public void OnTimeOutClose(MapObject map, int delay) {
        map.addMapOnceScriptEventTimer(getId(), "dealFail", delay);
    }


    /**
     * 每次战斗结束的处理函数
     * 玩家或者机器人死亡都要走这里
     */
    public void dealFinish(MapObject mapObject, Player player, Robot robot) {
        finishFlag = true;
        finishTime = Instant.now();
        /**
         * 清除这次的机器人，防止客户端显示多个机器人
         * */
        MapParam.getJJCParam(mapObject).put(JJCManager.JJC_RoundTime, 0);
        MapUtils.forceMoveTo(player, mapObject.getBrithPos());
        mapObject.addMapLoopScriptEventTimer(getId(), "clearRobot", 1, 1, 0);
    }

    //处理失败
    public void dealFail(MapObject mapObject, Player player) {

        try {
            Robot robot = (Robot) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Robert);
            MapParam.getJJCParam(mapObject).put(JJCManager.JJC_RoundTime, 0);
            Cfg_Clone_map_Bean config = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
            HashMap<Long, Long> itemMap = new HashMap<>();
            long actionId = IDConfigUtil.getLogId();
            List<Item> items = Item.createItems(config.getFail_reward());
            if (!items.isEmpty()) {
                sendNormalReward(player, player.getId(), items, ItemChangeReason.JJCBattleGet, actionId);
            }
            Manager.activityManager.cloneDropTrigger(player, config.getId());

            sendCharacterReward(player, false, itemMap, actionId);
            JJC jjc = Manager.jjcManager.getAlls().get(player.getId());

            int rank = jjc.getScore();
            mergeItemsToHashMap(items, itemMap);
            sendBattleInfo(player, false, 0, rank, robot.getName(), itemMap);
            player.changeCurPos(mapObject.getBrithPos());
            Manager.jjcManager.getChallengedLock().remove(player.getId());
            Manager.jjcManager.getChallengedLock().remove(robot.getMakerId());
            Manager.mapManager.manager().onQuitMap(mapObject, robot, true);
            JJCReport report = new JJCReport();
            report.setSucc(false);
            report.setTarget(robot.getName());
            report.setRank(rank);
            report.setLastRank(rank);
            report.setTime((int) (TimeUtils.Time() / 1000));
            report.setTiaozhao(true);
            jjc.records.add(report);
            if (jjc.records.size() > 10) {
                jjc.records.remove(0);
            }

            JJCDao handler = new JJCDao();
            handler.update(jjc);
            JJC other = Manager.jjcManager.getAlls().get(robot.getMakerId());
            if (other == null || other.getRoleId() > JJCManager.RobotMaxID) {
                return;
            }

            JJCReport oldreport = new JJCReport();
            oldreport.setSucc(true);
            oldreport.setTarget(player.getName());
            oldreport.setRank(other.getScore());
            oldreport.setLastRank(other.getScore());
            oldreport.setTime((int) (TimeUtils.Time() / 1000));
            oldreport.setTiaozhao(false);
            other.records.add(oldreport);
            if (other.records.size() > 10) {
                other.records.remove(0);
            }
            handler.update(other);

        } finally {
//            delayClose(clone);
        }
    }

    /**
     * 发送经验奖励
     */
    private void sendCharacterReward(Player player, boolean isSuccess, HashMap<Long, Long> itemMap, long actionId) {
        try {
            Cfg_Characters_Bean config = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (config == null) {
                return;
            }
            ReadLongArrayEs aiis = config.getJjc_reward_fail();
            if (isSuccess) {
                aiis = config.getJjc_reward_victory();
            }

            int type = aiis.get(0).get(0).intValue();
            long exp = aiis.get(0).get(1);

            if (Manager.currencyManager.manager().addEXP(player, aiis.get(0).get(1), ItemChangeReason.JJCBattleGet, actionId)) {
                itemMap.put((long) type, exp);
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }


    @Override
    public void onRobotDieBySecondKill(int mapModelId, Robot robot, Player player) {
        handleParticipateOnceJJC(player);
        handleAfterRobotDie(mapModelId, player, robot);

        Manager.copyMapManager.logic().biInstance(player, mapModelId, 2, 1, 0, false);
    }


    @Override
    public void OnRobotDie(MapObject mapObject, Robot robot, Fighter attacker) {
        boolean isOver = (boolean) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_End);
        if (isOver) {
            logger.info("OnRobotDie  --isOver");
            return;
        }

        Player player = (Player) MapParam.getJJCParam(mapObject).get(JJCManager.JJC_Player);
        dealFinish(mapObject, player, robot);
        handleAfterRobotDie(mapObject.getZoneModelId(), player, robot);
        Manager.jjcManager.getChallengedLock().remove(player.getId());
        Manager.jjcManager.getChallengedLock().remove(robot.getMakerId());

        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, 1, 0, false);
    }

    //发送战斗结果
    private void sendBattleInfo(Player player, boolean isSuc, int score, int rank, String robotName, HashMap<Long, Long> itemMap) {
        JJCMessage.ResJJCBattleReport.Builder msg = JJCMessage.ResJJCBattleReport.newBuilder();
        msg.setIsSuc(isSuc);
        msg.setScore(score);
        msg.setCurRank(rank);
        msg.setName(robotName);
        itemMap.entrySet().forEach(entry -> {
            msg.addRewardList(entry.getKey());
            msg.addRewardList(entry.getValue());
        });
        MessageUtils.send_to_player(player, JJCMessage.ResJJCBattleReport.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqJJCexit(Player player, JJCMessage.ReqJJCexit mess) {
        Manager.copyMapManager.manager().onReqCopyMapOut(player);
    }

    private void sort(int newsort, JJC rank) {
        Manager.peakManager.addCommand(new JJCSortICommand(newsort, rank));
    }

    private void robotExitBattle(Player player, Robot robot, MapObject mapObject) {
        /**
         * 把机器人从地图移除
         * */
        MapObject map = Manager.mapManager.getMap(robot.gainMapId());
        if (null == map) {
            logger.error("进入地图失败" + robot);
            return;
        }
        map.getRobots().remove(robot);
        Area area = Manager.mapManager.getArea(robot.gainCurPos(), map);
        if (null == area) {
            map.getRobots().remove(robot.getId());
            logger.error("进入区域失败" + robot);
            return;
        }
        area.getRobots().remove(robot);
        MapMessage.ResPlayerDisappear.Builder builder = MapMessage.ResPlayerDisappear.newBuilder();
        builder.addPlayerIds(robot.getId());
        if (null != robot.getPet()) {
            builder.addPlayerIds(robot.getPet().getId());
        }
        MessageUtils.send_to_player(player, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        /**
         * 从副本移除
         * */
        MapParam.getJJCParam(mapObject).remove(JJCManager.JJC_Robert);
        /**
         * 设置机器人状态
         * */
        robot.setState(EntityState.Dead.getValue());
    }

    private void sendFirstRewardMessage(boolean isChange, Player player, int newRank) {
        if (isChange) {
            JJCMessage.ResGetFirstReward.Builder getFirstRewardBuiler = JJCMessage.ResGetFirstReward.newBuilder();
            getFirstRewardBuiler.setRank(newRank);
            getFirstRewardBuiler.addAllRewardList(player.getJjcFirstRewardList());
            MessageUtils.send_to_player(player, JJCMessage.ResGetFirstReward.MsgID.eMsgID_VALUE, getFirstRewardBuiler.build().toByteArray());
        }
    }

    private void handleAfterRobotDie(int mapModelId, Player player, Robot robot) {
        Cfg_Clone_map_Bean config = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapModelId);
        HashMap<Long, Long> itemMap = new HashMap<>();
        List<Item> items = Item.createItems(config.getSuccess_reward());

        long actionId = IDConfigUtil.getLogId();
        if ( !items.isEmpty()) {
            sendNormalReward(player, player.getId(), items, ItemChangeReason.JJCBattleGet, actionId);
        }
        Manager.activityManager.cloneDropTrigger(player, config.getId());

        sendCharacterReward(player, true, itemMap, actionId);

        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        JJC other = Manager.jjcManager.getAlls().get(robot.getMakerId());
        int index = 0;
        if (other == null) {
            Cfg_Jjcrobot_Bean bb = CfgManager.getCfg_Jjcrobot_Container().getValueByKey(robot.getModelId());
            if (bb != null) {
                index = bb.getRank_max();
            }
        } else {
            index = other.getScore();
        }
        int oldRank = jjc.getScore();
        sort(index, jjc);
        int newRank = index;
        boolean isChange = oldRank != newRank;
        newRank = newRank > oldRank?oldRank:newRank;
        /**
         * 如果排名变化了，需要判断是否首次达到了某个等级
         * */
        sendFirstRewardMessage(isChange, player, newRank);
        if (0 == player.getJjcHistoryMaxRank()) {
            Manager.jjcManager.deal().setPlayerJJCHistoryRank(player, newRank);
            Manager.jjcManager.deal().sendRewardRedPoint(player);
        } else if (isChange && newRank < player.getJjcHistoryMaxRank()) {
            Manager.jjcManager.deal().setPlayerJJCHistoryRank(player, newRank);
            Manager.jjcManager.deal().sendRewardRedPoint(player);
        }
        if (isChange) {
            Manager.rankListManager.deal().setArenaRank(player, newRank);
        }
        mergeItemsToHashMap(items, itemMap);
        sendBattleInfo(player, true, 0, isChange ? newRank : jjc.getScore(), robot.getName(), itemMap);


        JJCReport report = new JJCReport();
        report.setSucc(true);
        report.setTarget(robot.getName());
        report.setLastRank(oldRank);
        report.setRank(newRank);
        report.setTime((int) (TimeUtils.Time() / 1000));
        report.setTiaozhao(true);
        jjc.records.add(report);
        if (jjc.records.size() > 10) {
            jjc.records.remove(0);
        }

        JJCDao handler = new JJCDao();
        handler.update(jjc);
        if (isChange && other != null && other.getRoleId() > JJCManager.RobotMaxID) {
            JJCReport oldreport = new JJCReport();
            oldreport.setSucc(false);
            oldreport.setTarget(player.getName());
            oldreport.setLastRank(newRank);
            oldreport.setRank(oldRank);
            oldreport.setTime((int) (TimeUtils.Time() / 1000));
            oldreport.setTiaozhao(false);
            other.records.add(oldreport);
            if (other.records.size() > 10) {
                other.records.remove(0);
            }
            handler.update(other);
        }
    }

    private void sendNormalReward(Player player, long roleId, List<Item> items, int reason, long actionId) {
        //玩家在线
        if (player != null) {

            int messageCode = Manager.backpackManager.manager().onHasAddSpaces(player, items);
            if (messageCode == 0) {
                StringBuilder goodsrewards = new StringBuilder();
                boolean isExp = getItemStr(items, goodsrewards);
                long oldexp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
                int oldlevel = player.getLevel();
                Manager.backpackManager.manager().addItems(player, items, reason, actionId);//加物品
                long exp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
                sendRewardMessage(player, JJCManager.modelId, goodsrewards, false, isExp, exp, oldexp, oldlevel);
                return;
            } else {
                MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, messageCode);
            }
        }

        // String content = MessageString.Copymap_RewardForMail + "@_@" + Manager.copyMapManager.getCopyMapName(JJCManager.modelId);
        // Manager.mailManager.sendMailToPlayer(roleId, MailType.CopymapSendMail, MessageString.Copymap_SYSTEM, MessageString.Copymap_MailTitle, content, items);

    }

    private boolean getItemStr(List<Item> items, StringBuilder goodsrewards) {
        boolean isHaveExp = false;
        for (Item item : items) {
            if (item.getItemModelId() == ItemCoinType.EXP) {
                isHaveExp = true;
            } else {
                if (goodsrewards.length() > 1) {
                    goodsrewards.append(",");
                }
                goodsrewards.append(Manager.backpackManager.manager().getChatInfo(item));
            }
        }
        return isHaveExp;
    }

    private void sendRewardMessage(Player player, int cpmodelId, StringBuilder goodsrewards, boolean isDanci, boolean isexp, long exp, long oldexp, int oldlevel) {
        String cloneName = Manager.copyMapManager.getCopyMapName(cpmodelId);
        if (isexp) {
            if (goodsrewards.length() > 0) {
                goodsrewards.append(",");
            }
            long num = exp - oldexp;
            long maxexpt = 0;
            if (oldlevel != player.getLevel()) {
                for (int level = oldlevel; level < player.getLevel(); ++level) {
                    Cfg_Characters_Bean model = CfgManager.getCfg_Characters_Container().getValueByKey(level);
                    if (model == null) {
                        continue;
                    }
                    maxexpt += model.getExp();
                }
                num = exp + maxexpt - oldexp;
            }
            goodsrewards.append("<t=2>").append(0).append(",").append(ItemCoinType.EXP).append(",").append("EXP").append("</t>");
            if (num > 1) {
                goodsrewards.append("<t=0>,,*");
                goodsrewards.append(num);
                goodsrewards.append("</t>");
            }

        }

        if (player == null || goodsrewards.length() < 1) {
            return;
        }
        logger.info(cloneName + " 获得：" + goodsrewards.toString());
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, isDanci ? MessageString.CopymapFujaHuaQu : MessageString.CopymapHuaQu, cloneName, goodsrewards.toString());
    }

    private void mergeItemsToHashMap(List<Item> items, HashMap<Long, Long> itemMap) {
        items.forEach(
                item -> {
                    Long value = itemMap.get(item.getItemModelId());
                    if (null == value) {
                        itemMap.put((long) item.getItemModelId(), item.realNum());
                    } else {
                        itemMap.put((long) item.getItemModelId(), value + item.realNum());
                    }
                }
        );
    }

    private void handleParticipateOnceJJC(Player player) {
        Manager.countManager.addVariant(player, VariantType.JJCAccumulationCount, 1);
        Manager.controlManager.operate(player, FunctionVariable.ArenaChallenge, 1);
        Manager.controlManager.operate(player, FunctionVariable.ArenaNum, 1);
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.JJC, 1);
        Manager.retrieveResManager.getScript().count(player, RetrieveType.JJC);
    }



    /**
     * 一键扫荡
     * @param player
     */
    public void OneKeySweep(Player player,int count){


        Manager.countManager.addVariant(player, VariantType.JJCAccumulationCount, count);
        Manager.controlManager.operate(player, FunctionVariable.ArenaChallenge, count);
        Manager.controlManager.operate(player, FunctionVariable.ArenaNum, count);
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.JJC, count);
        Manager.retrieveResManager.getScript().count(player, RetrieveType.JJC,count);



        HashMap<Long, Long> itemMap = new HashMap<>();
        Cfg_Clone_map_Bean config = CfgManager.getCfg_Clone_map_Container().getValueByKey(JJCManager.modelId);
        JJC jjc = Manager.jjcManager.getAlls().get(player.getId());
        for (int i=0;i < count;i++){
            List<Item> items = Item.createItems(config.getSuccess_reward());
            long actionId = IDConfigUtil.getLogId();
            if ( !items.isEmpty()) {
                sendNormalReward(player, player.getId(), items, ItemChangeReason.JJCBattleGet, actionId);
            }
            Manager.activityManager.cloneDropTrigger(player, config.getId());
            sendCharacterReward(player, true, itemMap, actionId);
            mergeItemsToHashMap(items, itemMap);
        }
        sendBattleInfo(player, true, 0, jjc.getScore(), "", itemMap);

        Manager.copyMapManager.logic().biInstance(player,  JJCManager.modelId, 2, 1, 0, false);

    }
}
