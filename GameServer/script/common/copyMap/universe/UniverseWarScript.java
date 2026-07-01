package common.copyMap.universe;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Monster_Container;
import com.data.container.Cfg_Universe_boss_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.boss.struct.BossTypeConst;
import com.game.chat.structs.Notify;
import com.game.command.structs.CommandData;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.DynamicBlock;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.structs.SynToFightType;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.title.structs.TitleData;
import com.game.universe.script.IUniverseWarScript;
import com.game.universe.struct.GuildBattleInfo;
import com.game.universe.struct.UniverseWarData;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.json.TypeReference;
import game.core.map.Position;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 太虚战场流程脚本
 */
public class UniverseWarScript implements IMapBaseScript, ICopyReliveScript, IUniverseWarScript {

    private static final Logger log = LogManager.getLogger(UniverseWarScript.class);

    /**
     * 最大阵营数量
     */
    private static final int maxCamp = 4;
    /**
     * 排行榜最大显示数量
     */
    private static final int maxRank = 10;
    /**
     * 地图动态阻挡名称
     */
    private static final String[][] blocks = {{"DynamicBlocker5", "DynamicBlocker6"}, {"DynamicBlocker1", "DynamicBlocker2"}, {"DynamicBlocker7", "DynamicBlocker8"}, {"DynamicBlocker3", "DynamicBlocker4"}};
    /**
     * 地图传送点
     */
    private static int[][] transports = {{7, 8}, {1, 2}, {3, 4}, {5, 6}};

    @Override
    public void onCreate(MapObject map, Object... objects) {
        map.setAutoRemove(false);
        long endTime = 0;
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        List<CommonMessage.CrossAttribute> createParams = (List<CommonMessage.CrossAttribute>) objects[1];
        for (CommonMessage.CrossAttribute cp : createParams) {
            if (cp.getType() == 1) {
                endTime = cp.getValue();
                String param = cp.getParam();
                String[] params = param.split(",");
                int groupID = Integer.parseInt(params[0]);
                int worldLevel = Integer.parseInt(params[1]);
                //当前组的平均世界等级
                uwData.setWorldLevel(worldLevel);
                //跨服副本分组
                uwData.setCrossServerGroupId(groupID);
            } else if (cp.getType() == 2) {
                String param = cp.getParam();
                log.info("guildBattleInfo:" + param);
                if (param != null && !param.equals("") && !param.equals("{}")) {

                    ConcurrentHashMap<String, ConcurrentHashMap<Integer, GuildBattleInfo>> guildBattleMap = JsonUtils.parseObject(param, new TypeReference<ConcurrentHashMap<String, ConcurrentHashMap<Integer, GuildBattleInfo>>>() {
                    });
                    if (guildBattleMap != null && guildBattleMap.size() > 0) {
                        Manager.universeManager.getGuildBattleInfoMap().putAll(guildBattleMap);
                    }
                }
            }
        }
        //创建地图初始化怪物
        monsterEnterMap(map);
        //初始化传送点
        Map<Integer, Integer> tMap;
        for (int i = 1; i <= 4; i++) {
            tMap = new HashMap<>(2);
            tMap.put(transports[i - 1][0], transports[i - 1][1]);
            tMap.put(transports[i - 1][1], transports[i - 1][0]);
            uwData.getTransportMap().put(i, tMap);
        }
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        if (bean == null) {
            log.error(String.format("太虚战场{%s}在表中Cfg_Clone_map_Bean无配置!!!! key=", map.getZoneModelId()));
            return;
        }
        //准备时间完毕开启第一层阻挡
        long nowTime = TimeUtils.Time();
        long lastTime = endTime - nowTime;
        if (bean.getExist_time() - lastTime >= bean.getEnter_time()) {
            readyBlock(map);
        } else {
            long readyTime = bean.getEnter_time() - (bean.getExist_time() - lastTime);
            log.info("副本结束时间：" + TimeUtils.format2string(endTime) + ",准备阻挡剩余时间：" + readyTime + "ms");
            map.addMapOnceScriptEventTimer(getId(), "readyBlock", readyTime);
        }
        log.info("副本结束时间：" + TimeUtils.format2string(endTime) + ",endTime=" + endTime);
        //副本结束前五分钟提示
        map.addMapOnceScriptEventTimer(getId(), "endNotice", lastTime - 5 * 60 * 1000);
        map.addMapOnceScriptEventTimer(getId(), "timeOutClose", lastTime + 10 * 1000);
        log.error(map.getId() + "_" + map.getMapModelId() + "_" + map.getZoneModelId() + "_" + map.getName() + "创建成功,groupId=" + uwData.getCrossServerGroupId() + ",worldLv=" + uwData.getWorldLevel());
    }

    private void monsterEnterMap(MapObject mapObject) {
        UniverseWarData universeWarData = MapParam.getUniverseWarData(mapObject);
        long nowTime = TimeUtils.Time();
        for (Cfg_Universe_boss_Bean bossBean : Cfg_Universe_boss_Container.GetInstance().getValuees()) {
            ReadIntegerArray rs = bossBean.getWorldLevel();
            if (!isRange(universeWarData.getWorldLevel(), rs.get(0), rs.get(1))) {
                continue;
            }
            Monster monster = MonsterManager.getInstance().createMonster(bossBean.getMonsterID());
            if (monster != null) {
                monster.changeLine(mapObject.getLineId());
                monster.changeMapId(mapObject.getId());
                monster.changeMapModelId(mapObject.getMapModelId());
                Position position = new Position();
                position.setX(bossBean.getPos().get(0).get(0));
                position.setY(bossBean.getPos().get(0).get(1));
                monster.setInitPos(position);
                monster.setCamp(5 * 100);
                Manager.mapManager.manager().onEnterMap(monster);
                universeWarData.getRefreshMap().put(monster.getModelId(), nowTime);
                //关注刷怪tip通知
                if (bossBean.getCreateTips() == 1) {
                    sendF2PBossRefreshTip(MapParam.getUniverseWarData(mapObject).getCrossServerGroupId(), bossBean.getId(), BossTypeConst.UNIVERSE_WAR_BOSS);
                }
            } else {
                log.error("太虚战场怪物生成失败：monsterId=" + bossBean.getMonsterID());
            }
        }
//        log.info("太虚战场怪物生成完成,怪物数量："+mapObject.getMonsters().size());
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.TerritorialWarCelebrity)) {
            return false;
        }
        if (TimeUtils.getOpenServerDay() < Global.UniverseSeverOpenTime) {
            return false;
        }
        int anger = (int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        if (anger >= Global.Universe_Anger_Limit) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_ACTIVITY_BOSS_ANGER_LIMIT);
            return false;
        }
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        if (dailyBean == null) {
            return false;
        }
        int cloneId = getCurCloneId(dailyBean);
        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneId);
        if (cloneBean == null) {
            return false;
        }
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(cloneBean.getMapid());
        if (null == mapBean) {
            return false;
        }
        if (mapBean.getCan_team() == 0 && player.getTeamId() > 0) {
            return false;
        }
        return true;
    }

    private int getCurCloneId(Cfg_Daily_Bean bean) {
        long nowTime = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(nowTime) * 60 + TimeUtils.getDayOfMin(nowTime);
        for (int i = 0; i < bean.getTime().size(); i++) {
            int startMin = bean.getTime().get(i).get(0);
            int endMin = bean.getTime().get(i).get(1);

            if (startMin <= nowMin && nowMin <= endMin) {
                return bean.getCloneID().get(i);
            }
        }
        return bean.getCloneID().get(0);
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        //分配阵营
        int curAnger = (int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
        initCamp(player, uwData, curAnger);
        int camp = getWarCamp(map, player);
        //初始化玩家出生点
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        ReadIntegerArrayEs born = bean.getBornPosition();
        Position pos = MapManager.getPos(born.get(camp - 1).get(0), born.get(camp - 1).get(1));
        Manager.mapManager.changeMap(player, map.getId(), pos, false);
        //检查阵营buff
        checkBuff(player, uwData, camp);
        //同步地图怪物信息
        sendF2GResUniverseWarPanel(player, map, uwData, curAnger);

        //检查指挥官
        enterCheckCommander(player, map, uwData);

        log.error(player.playerCrossData.platSid + player.getInfo() + "进入太虚战场,玩家阵营:" + camp + ",当前阵营总数：" + uwData.getCampMap().size());
    }

    private void checkBuff(Player player, UniverseWarData uwData, int camp) {
        if (uwData.getOpenCamps().contains(camp)) {//守关boss已经死了，清除buff
            for (Integer buffId : uwData.getBuffList()) {
                Manager.buffManager.deal().onRemoveBuff(player, buffId);
            }
        } else {
            for (Integer buffId : uwData.getBuffList()) {
                Manager.buffManager.deal().onAddBuff(player, player, buffId);
            }
        }
    }

    private void initCamp(Player player, UniverseWarData uwData, int curAnger) {
        String key = player.playerCrossData.platSid;
        if (!uwData.getCampMap().keySet().contains(key)) {
            uwData.getCampMap().put(key, uwData.getCampMap().size() + 1);
        }

        if (curAnger >= Global.Universe_Anger_Limit) {
            player.setCamp(5 * 100);
        } else {
            player.setCamp(uwData.getCampMap().get(key));
        }
    }

    /**
     * 同步怪物信息
     *
     * @param player
     * @param mapObject
     */
    private void synMonsterInfo(Player player, MapObject mapObject) {
        UniverseWarData universeWarData = MapParam.getUniverseWarData(mapObject);
        MSG_UniverseMessage.ResUpdateMonsterRefresh.Builder msg = MSG_UniverseMessage.ResUpdateMonsterRefresh.newBuilder();
        for (Cfg_Universe_boss_Bean bean : Cfg_Universe_boss_Container.GetInstance().getValuees()) {
            int camp = getWarCamp(mapObject, player);
            if (bean.getCamp() != 5 && camp != bean.getCamp()) {//只发送玩家对应阵营（排除中心区域怪物）
                continue;
            }
            ReadIntegerArray rs = bean.getWorldLevel();
            if (!isRange(universeWarData.getWorldLevel(), rs.get(0), rs.get(1))) {
                continue;
            }
            MSG_UniverseMessage.UniverseMonsterInfo.Builder builder = bossToBossInfoBulild(bean, mapObject);
            msg.addMonsterInfos(builder);
        }
        MessageUtils.send_to_player(player, MSG_UniverseMessage.ResUpdateMonsterRefresh.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private MSG_UniverseMessage.UniverseMonsterInfo.Builder bossToBossInfoBulild(Cfg_Universe_boss_Bean bean, MapObject mapObject) {
        MSG_UniverseMessage.UniverseMonsterInfo.Builder bossInfo = MSG_UniverseMessage.UniverseMonsterInfo.newBuilder();
        bossInfo.setModelId(bean.getId());
        bossInfo.setCare(false);
        bossInfo.setType(bean.getType());
        Map<Integer, Long> dieMap = MapParam.getUniverseWarData(mapObject).getDieMap();
        if (dieMap.keySet().contains(bean.getMonsterID())) {
            Long refreshTime = MapParam.getUniverseWarData(mapObject).getRefreshMap().get(bean.getMonsterID());
            if (refreshTime == null) {
                bossInfo.setRefreshTime(-1);
            } else {
                bossInfo.setRefreshTime((int) ((refreshTime - TimeUtils.Time()) / 1000));
            }
        } else {
            bossInfo.setRefreshTime(0);//存活
        }
//        log.info("=========刷新剩余时间："+bossInfo.getRefreshTime()+",monsterConfigId="+bossInfo.getModelId());
        return bossInfo;
    }

    private int getWarCamp(MapObject mapObject, Player player) {
        return MapParam.getUniverseWarData(mapObject).getCampMap().get(player.playerCrossData.platSid);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        //移除阵营buff
        for (Integer buffId : uwData.getBuffList()) {
            Manager.buffManager.deal().onRemoveBuff(player, buffId);
        }
//        MessageUtils.notify_player(player, Notify.CHAT, MessageString.UNIVERSE_MAP_CLOSE_TITLE);

        //处理指挥相关
        ConcurrentHashMap<String, CommandData> commandDataMap = Manager.commandManager.getCommandDataMap().get(map.getId());
        if (commandDataMap == null) {
            return;
        }

        CommandData cmd = commandDataMap.get(player.playerCrossData.platSid);
        if (cmd != null) {
            if (cmd.getCommanderId() == player.getId()) {
                cmd.setCommanderId(0);
                //卸下称号
                Manager.titleManager.deal().onReqDownTitle(player, Global.Universe_Command_Title);
            } else {
                cmd.getRoleList().remove(player.getId());
            }
            //BUFF移除
            Manager.buffManager.deal().onRemoveBuff(player, cmd.getBuffId());

            if (map.getPlayers().size() <= 0) {
                return;
            }

            Collection<Player> list = getCampPlayers(map.getPlayers().values(), player.playerCrossData.platSid);
            if (cmd.getCommanderId() <= 0) {//没有指挥官
                //重新选择指挥官
                if (list != null && list.size() > 0) {
                    dealCommander(map, list, cmd, player.playerCrossData.platSid, false, true);
                }
            } else {//有指挥官
                Player commander = map.getPlayer(cmd.getCommanderId());
                if (commander != null) {
                    //通知指挥官人数变化
                    sendCommandInfo(commander, cmd.getCommanderId(), cmd.getTargetId(), getCMDNum(cmd));
                }

                if (cmd.getRoleList().size() > 0) {
                    //通知队伍人数变化
                    noticeCMDInfo(map, list, cmd);
                }

                //更新队伍BUFF
                updateCmdBuff(player, cmd);
            }
        }
    }

    private Collection<Player> getCampPlayers(Collection<Player> players, String platSid) {
        List<Player> list = new ArrayList<>();
        for (Player player : players) {
            if (platSid.equals(player.playerCrossData.platSid)) {
                list.add(player);
            }
        }
        return list;
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        Cfg_Universe_boss_Bean bean = getCfgByMonsterModelId(monster.getModelId());
        if (bean == null) {
            log.error(String.format("太虚战场boss{%s}在表中Cfg_Universe_boss_Bean无配置!!!!", monster.getModelId()));
            return;
        }
        Map<Integer, Long> dieMap = MapParam.getUniverseWarData(map).getDieMap();
        dieMap.put(monster.getModelId(), monster.getId());
        //发放掉落奖励奖
        Manager.dropManager.deal().specialDropReward(monster, (Player) attacker, SpecialDropDefine.UNIVERSE, true, -1);
        updateAnger(bean, map, monster);
        //发放阵营排名奖励
        String killServer = sendServerReward(bean, map, monster);
        //检查第二级阻挡开启
        checkCampBossDie(map, bean);
        if (bean.getGroup() == 2) {
            MessageUtils.notify_Map(map, Notify.FIXED, MessageString.UniverseWarDeath, killServer);
        }

        for (Player player : map.getPlayers().values()) {
            if (player == null) {
                continue;
            }

            int camp = getWarCamp(map, player);
            if (camp != bean.getCamp()) {
                continue;
            }

            MessageUtils.notify_player(player, Notify.FIXED, MessageString.UniversePriServerDeath, monster.getName());
        }

        //更新指挥队列信息
        updateCommandTarget(map, monster);
    }

    private Cfg_Universe_boss_Bean getCfgByMonsterModelId(int monsterModelId) {
        for (Cfg_Universe_boss_Bean bossBean : Cfg_Universe_boss_Container.GetInstance().getValuees()) {
            if (bossBean.getMonsterID() == monsterModelId) {
                return bossBean;
            }
        }
        return null;
    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        Cfg_Universe_boss_Bean bean = getCfgByMonsterModelId(monster.getModelId());
        if (bean == null) {
            log.error(String.format("太虚战场{%s}在表中Cfg_Universe_boss_Bean无配置!!!!", monster.getModelId()));
            return;
        }
//        if (bean.getGroup() == 1 || bean.getGroup() == 2) {//BOSS不会再刷新
//            return;
//        }
        //需要同步刷新时间
        long now = TimeUtils.Time();
        long nextTime;
        if (bean.getResp_time() != 0) {
            nextTime = now + bean.getResp_time() * 60 * 1000;
        } else {
            nextTime = findNextRefshTime(now, bean.getSpecial_time());
        }
        MapParam.getUniverseWarData(map).getRefreshMap().put(monster.getModelId(), nextTime);
        //同步怪物信息
        synMonsterUpdateAllMap(bean, map);
        Manager.bossManager.manager().addBossKilledRecord(map, monster, (Player)attacker);
        long betweenTime = nextTime - now; //取秒
        long refreshTime = 0;
        if (betweenTime > 0) {
            refreshTime = betweenTime;
            if (betweenTime > Global.Boss_attent_notice * 1000) {
                map.addMapOnceScriptEventTimer(getId(), "noticeBoss", betweenTime - Global.Boss_attent_notice * 1000, bean.getId());
            } else {//提前一分钟通知
                sendF2PBossRefreshTip(MapParam.getUniverseWarData(map).getCrossServerGroupId(), bean.getId(), BossTypeConst.UNIVERSE_WAR_BOSS);
            }
            map.addMapOnceScriptEventTimer(getId(), "refreshBoss", refreshTime, monster.getModelId());
            log.info("刷新时间：" + bean.getId() + "_" + monster.getModelId() + "_" + TimeUtils.format2string(nextTime));
        } else if (betweenTime == 0) {
            refreshBoss(map, monster.getModelId());
        } else {//时间有问题，可能是GM命令修改时间导致

        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 根据凌晨0点 间隔多少分钟刷新
     *
     * @param special_time
     * @return
     */
    private long findNextRefshTime(long nowTime, ReadIntegerArray special_time) {
        long todayBeginTime = TimeUtils.getTodayBeginTime();
        Integer[] timeNodes = special_time.getValue();
        for (int i = 0; i < timeNodes.length; i++) {
            Integer timeNode = timeNodes[i];
            long refshTime = todayBeginTime + timeNode * 60 * 1000;
            if (refshTime > nowTime) {
                return refshTime;
            }
            //最后一个了
            if (i + 1 == timeNodes.length) {
                long nextFirstRes = TimeUtils.getTodayBeginTime() + 24 * 3600 * 1000 + timeNodes[0] * 60 * 1000;
                return nextFirstRes;
            }
        }
        return 0;
    }

    /**
     * 检查守关boss死亡
     */
    private void checkCampBossDie(MapObject map, Cfg_Universe_boss_Bean bean) {
        if (bean.getGroup() != 1) {
            return;
        }

//        String blockName = blocks[bean.getCamp() - 1][1];
//        //开启对应阵营阻挡
//        DynamicBlock door = map.getDoors().get(blockName);
//        if (null == door) {
//            return;
//        }
//        door.setOpen(true);
//        log.error("太虚战场守关BOSS死亡,阵营阻挡"+blockName+"开启，阵营:" + bean.getCamp());

        //阵营buff更新
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        uwData.getBuffList().add(bean.getKill_buff());
        uwData.getOpenCamps().add(bean.getCamp());

        String killServer = "";
        //通知对应阵营玩家对应的阻挡开启了
        for (Player player : map.getPlayers().values()) {
            if (player == null) {
                continue;
            }
//            MapMessage.ResUpdateBlockDoor.Builder msg = MapMessage.ResUpdateBlockDoor.newBuilder();
//            msg.setId(blockName);
//            msg.setIsopen(true);
//            MessageUtils.send_to_player(player, MapMessage.ResUpdateBlockDoor.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            int camp = getWarCamp(map, player);
            if (camp != bean.getCamp()) {//其他阵营玩家增加buff
                if (!uwData.getOpenCamps().contains(camp)) {//该阵营还没有击杀守关boss
                    Manager.buffManager.deal().onAddBuff(player, player, bean.getKill_buff());
                }
            } else {//当前阵营的守关boss死亡，清除阵营buff
                if (killServer.equals("")) {
                    killServer = String.valueOf(player.getCurServerId());
                }
                for (Integer buffId : MapParam.getUniverseWarData(map).getBuffList()) {
                    Manager.buffManager.deal().onRemoveBuff(player, buffId);
                }
            }
        }
        MessageUtils.notify_Map(map, Notify.FIXED, MessageString.UniversePassWayOpen, killServer);
    }

    /**
     * 通知boss刷新
     *
     * @param map
     * @param configId
     */
    private void noticeBoss(MapObject map, int configId) {
        sendF2PBossRefreshTip(MapParam.getUniverseWarData(map).getCrossServerGroupId(), configId, BossTypeConst.UNIVERSE_WAR_BOSS);
    }

    private void endNotice(MapObject map) {
//        for (Player player : map.getPlayers().values()) {
//            MessageUtils.notify_player(player, Notify.FIXED, MessageString.UniverseActivityEnd);
//        }
    }

    private void timeOutClose(MapObject map) {
        List<Integer> buffList = MapParam.getUniverseWarData(map).getBuffList();
        for (Player player : map.getPlayers().values()) {
            //移除阵营buff
            for (Integer buffId : buffList) {
                Manager.buffManager.deal().onRemoveBuff(player, buffId);
            }
            MessageUtils.notify_player(player, Notify.CHAT, MessageString.UNIVERSE_MAP_CLOSE_TITLE);
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        map.setStop(true);
    }

    /**
     * 发送奖励
     */
    private void updateAnger(Cfg_Universe_boss_Bean bean, MapObject map, Monster monster) {
        int addAnger;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p = (Player) hatred.getTarget();
            //更新怒气值
            if (bean.getRage() != 0) {
                int curAnger = (int) Manager.countManager.getCount(p, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
                if (bean.getRage() + curAnger >= Global.Universe_Anger_Limit) {
                    addAnger = Global.Universe_Anger_Limit - curAnger;
                    //怒气满了切换为怪物阵营
                    p.setCamp(5 * 100, true);
                } else {
                    addAnger = bean.getRage();
                }
                Manager.countManager.addCount(p, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue(), Count.RefreshType.CountType_Forever, addAnger);
                //临时用这个更新怒气值
                Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, p, (long) addAnger);
            }
        }
    }

    private String sendServerReward(Cfg_Universe_boss_Bean bean, MapObject map, Monster monster) {
        String killServer = "";
        int rank = 0;
        //<camp,damage>
        Map<Integer, Long> sDamageMap = new HashMap<>();
        //map<camp,<roleId,rank>>
        Map<Integer, Map<Player, Integer>> campRankMap = new HashMap<>();
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p = (Player) hatred.getTarget();
            //根据阵营分配
            int camp = getWarCamp(map, p);
            if (campRankMap.containsKey(camp)) {
                campRankMap.get(camp).put(p, ++rank);
                sDamageMap.put(camp, sDamageMap.get(camp) + hatred.getHatred());
            } else {
                Map<Player, Integer> pDamageMap = new HashMap<>();
                pDamageMap.put(p, ++rank);
                campRankMap.put(camp, pDamageMap);
                sDamageMap.put(camp, hatred.getHatred());
            }

            ConcurrentHashMap<String, CommandData> cmdMap = Manager.commandManager.getCommandDataMap().get(map.getId());
            if (cmdMap != null) {
                CommandData cmd = cmdMap.get(p.playerCrossData.platSid);
                if (cmd != null && cmd.getCommanderId() > 0) {
                    if (p.getId() == cmd.getCommanderId()) {
                        Cfg_Universe_command_Bean cmdBean = getCMDConfig(getCMDNum(cmd));
                        if (cmdBean != null) {
                            ReadIntegerArrayEs rewards = cmdBean.getReward();
                            if (rewards != null) {
                                List<Item> createItems = Item.createItems(rewards);
                                Manager.crossServerManager.getCrossServer().sendMailReward(p, String.valueOf(MessageString.System)
                                        , String.valueOf(MessageString.Universe_Command_Reward_Mail_Title)
                                        , new StringBuilder(String.valueOf(MessageString.Universe_Command_Reward_Mail)).append("@_@").append(monster.getName()).toString()
                                        , createItems, ItemChangeReason.UniverseReward);
                            }
                        }
                    }
                }
            }

            //参与击杀者获得的积分奖励
            List<Item> createItems = Item.createItems(ItemCoinType.UniversePoint, bean.getPoint(), true);
            Manager.crossServerManager.getCrossServer().sendReward(p, createItems, ItemChangeReason.UniverseReward);
            MessageUtils.notify_player(p, Notify.NORMAL, MessageString.Universe_Kill_BOSS_Point, String.valueOf(bean.getPoint()));
        }

        if (bean.getRankReward() == null || bean.getRankReward().isEmpty()) {
            return killServer;
        }

        TreeMap<Long, Integer> rankMap = new TreeMap<>(Comparator.comparingLong(n -> (long) n).reversed());
        for (Map.Entry<Integer, Long> entry : sDamageMap.entrySet()) {
            rankMap.put(entry.getValue(), entry.getKey());
        }

        //服务器伤害排行
        rank = 0;
        //<camp,rank>
        Map<Integer, Integer> sCampRankMap = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : rankMap.entrySet()) {
            sCampRankMap.put(entry.getValue(), ++rank);
        }

        //发奖
        StringBuilder mailContent;
        for (Map.Entry<Integer, Map<Player, Integer>> entry : campRankMap.entrySet()) {
            int camp = entry.getKey();
            ReadIntegerArray ria = bean.getServerRankReward();
            int sRank = sCampRankMap.get(camp);
            int sTimes = ria.get(sRank - 1);
            if (sTimes <= 0) {
                continue;
            }
            for (Map.Entry<Player, Integer> e : entry.getValue().entrySet()) {
                Player p = e.getKey();
                int pRank = e.getValue();
                int index = getIndex(pRank, bean.getRankLimit());
                if (index < 0) {
                    continue;
                }
                if (killServer.equals("") && sRank == 1) {
                    killServer = String.valueOf(e.getKey().getCurServerId());
                }
                ReadArray<Integer> rs = bean.getRankReward().get(index);
                List<Item> createItems = Item.createItems(rs.get(0), rs.get(1) * sTimes, true);
                log.info(p.getInfo() + "个人排名:" + pRank + ",服务器排名：" + sRank + ",服务器奖励倍数:" + sTimes + ",阵营:" + camp);
                mailContent = new StringBuilder(String.valueOf(MessageString.UNIVERSE_BOSS_REWARD_MAIL_TEXTURE));
                mailContent.append("@_@").append(monster.getName()).append("@_@").append(pRank).append("@_@").append(sRank);
                Manager.crossServerManager.getCrossServer().sendMailReward(p, String.valueOf(MessageString.System)
                        , String.valueOf(MessageString.UNIVERSE_BOSS_REWARD_MAIL_TITLE)
                        , mailContent.toString()
                        , createItems, ItemChangeReason.UniverseReward);
            }
        }
        return killServer;
    }

    private int getIndex(int rank, ReadIntegerArrayEs rias) {
        for (int i = 0; i < rias.size(); i++) {
            ReadArray<Integer> rs = rias.get(i);
            int minRank = rs.get(0);
            int maxRank = rs.size() == 1 ? rs.get(0) : rs.get(1);
            if (isRange(rank, minRank, maxRank)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRange(int rank, int minRank, int maxRank) {
        if (rank >= minRank && rank <= maxRank) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "readyBlock":
                readyBlock(map);
                break;
            case "refreshBoss":
                refreshBoss(map, (int) params[0]);
                break;
            case "noticeBoss":
                noticeBoss(map, (int) params[0]);
                break;
            case "endNotice":
                endNotice(map);
                break;
            case "timeOutClose":
                timeOutClose(map);
                break;
        }
    }

    private void readyBlock(MapObject map) {
        //通知地图所有玩家阻挡打开
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        for (int i = 0; i < maxCamp; i++) {
            DynamicBlock door = map.getDoors().get(blocks[i][0]);
            if (null == door) {
                return;
            }
            door.setOpen(true);
        }
        uwData.setOpen(true);
        log.error(map.getName() + "_" + map.getMapModelId() + "_" + map.getId() + "准备时间结束打开阻挡");

        if (uwData.getCampMap().isEmpty()) {//没有玩家进入副本
            return;
        }

        for (Player player : map.getPlayers().values()) {
            if (player == null) {
                continue;
            }

            int camp = getWarCamp(map, player);
            String blockName = blocks[camp - 1][0];
            if (blockName == null) {
                continue;
            }
            //通知对应阵营玩家对应的阻挡开启了
            MapMessage.ResUpdateBlockDoor.Builder msg = MapMessage.ResUpdateBlockDoor.newBuilder();
            msg.setId(blockName);
            msg.setIsopen(true);
            MessageUtils.send_to_player(player, MapMessage.ResUpdateBlockDoor.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        //阻挡打开初始化指挥官
        initCommander(map);
    }

    private void updateCommandTarget(MapObject map, Monster monster) {
        ConcurrentHashMap<String, CommandData> cmdMap = Manager.commandManager.getCommandDataMap().get(map.getId());
        if (cmdMap == null) {
            return;
        }

        String cmdName = "";
        StringBuilder context = new StringBuilder(String.valueOf(MessageString.Universe_Kill_BOSS_Notice));

        for (Map.Entry<String, CommandData> entry : cmdMap.entrySet()) {
            CommandData cmd = entry.getValue();
            if (cmd.getTargetId() != monster.getModelId()) {
                continue;
            }

            cmd.setTargetId(0);

            context = new StringBuilder(String.valueOf(MessageString.Universe_Kill_BOSS_Notice));
            Player commander = map.getPlayer(cmd.getCommanderId());
            if(commander!=null){
                cmdName = commander.getName();
            }
            context.append("@_@").append(cmdName).append("@_@").append(monster.getName());

            for (Player p : map.getPlayers().values()) {
                if (!entry.getKey().equals(p.playerCrossData.platSid)) {
                    continue;
                }

                if (cmd.getCommanderId() == p.getId()) {
                    sendCommandInfo(p, cmd.getCommanderId(), 0, getCMDNum(cmd));
                    sendCommandBulletScreen(p, context.toString(), true);
                    continue;
                }

                if (cmd.getRoleList().contains(p.getId())) {
                    sendCommandInfo(p, cmd.getCommanderId(), 0, getCMDNum(cmd));
                }

                sendCommandBulletScreen(p, context.toString(), true);
            }
        }
    }

    @Override
    public void updateCmdBuff(Player player, CommandData cmd) {
        int oldBuffId = cmd.getBuffId();
        Cfg_Universe_command_Bean bean = getCMDConfig(getCMDNum(cmd));
        int newBuffId = 0;
        if (bean != null) {
            newBuffId = bean.getBuff();
        }

//        log.info("============buffId="+newBuffId+",num="+getCMDNum(cmd));
        cmd.setBuffId(newBuffId);

        MapObject mapObject = Manager.mapManager.getMap(player.getCurGps().getMapId());

        //指挥官加buff
        Player commander;
        if (cmd.getCommanderId() == player.getId()) {
            commander = player;
        } else {
            commander = mapObject.getPlayer(cmd.getCommanderId());
        }

        if (commander != null) {
            if (oldBuffId > 0 && oldBuffId != newBuffId) {//移除之前的buff
                Manager.buffManager.deal().onRemoveBuff(commander, oldBuffId);
            }
            Manager.buffManager.deal().onAddBuff(commander, commander, newBuffId);
        }

        //队友加buff
        for (long roleId : cmd.getRoleList()) {
            Player p = mapObject.getPlayer(roleId);
            if (p == null) {
                continue;
            }
            if (!player.playerCrossData.platSid.equals(p.playerCrossData.platSid)) {
                continue;
            }

            if (!cmd.getRoleList().contains(p.getId())) {
                continue;
            }

            if (oldBuffId > 0 && oldBuffId != newBuffId) {//移除之前的buff
                Manager.buffManager.deal().onRemoveBuff(p, oldBuffId);
            }
            Manager.buffManager.deal().onAddBuff(p, p, newBuffId);
        }
    }

    private Cfg_Universe_command_Bean getCMDConfig(int num) {
        if (num <= 1) {
            return null;
        }
        Cfg_Universe_command_Bean result = null;
        for (Cfg_Universe_command_Bean bean : CfgManager.getCfg_Universe_command_Container().getValuees()) {
            if (bean == null) {
                continue;
            }
            if (num < bean.getCount()) {
                break;
            }
            result = bean;
        }
        return result;
    }

    private void initCommander(MapObject map) {
        log.info("阻挡打开，初始化各阵营指挥官");
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        if (uwData == null) {
            return;
        }
        ConcurrentHashMap<String, CommandData> commandDataMap = Manager.commandManager.getCommandDataMap().get(map.getId());
        if (commandDataMap == null) {
            commandDataMap = new ConcurrentHashMap<>();
            Manager.commandManager.getCommandDataMap().put(map.getId(), commandDataMap);
        }

        for (String platSid : uwData.getCampMap().keySet()) {
            CommandData cmd = commandDataMap.get(platSid);
            if (cmd == null) {
                cmd = new CommandData();
                commandDataMap.put(platSid, cmd);
            }
            dealCommander(map, map.getPlayers().values(), cmd, platSid, true, false);
        }
    }

    /**
     * 选择指挥官
     *
     * @param players
     * @param cmd
     * @param platSid
     */
    private void dealCommander(MapObject map, Collection<Player> players, CommandData cmd, String platSid, boolean synAll, boolean isChange) {
        long roleId;
        ConcurrentHashMap<Integer, GuildBattleInfo> guildBattleInfo = Manager.universeManager.getGuildBattleInfoMap().get(platSid);
        if (guildBattleInfo == null || guildBattleInfo.size() <= 0) {
            roleId = findHighestFightPower(players, platSid);
        } else {
            roleId = findCommander(players, guildBattleInfo, platSid);
            if (roleId <= 0L) {
                roleId = findHighestFightPower(players, platSid);
            }
        }

        if (roleId <= 0L) {
            log.info("========没有找到的指挥官！！！ 阵营：" + platSid);
            return;
        }

        //佩戴称号
        Player commander = getPlayer(players, roleId);
        if (commander == null) {
            log.info("========没有找到的指挥官玩家对象！！！ 阵营：" + platSid);
            return;
        }
        wearTitle(commander);

        cmd.setCommanderId(roleId);

        if (cmd.getRoleList().contains(roleId)) {//从队伍中移除
            cmd.getRoleList().remove(roleId);
        }

        if (isChange) {
            send_to_camp(players, platSid, MessageString.Universe_New_Command, commander.getName());
        } else {
            send_to_camp(players, platSid, MessageString.Universe_Command_Assign, commander.getName());
        }

        int oldBuffId = cmd.getBuffId();
        Cfg_Universe_command_Bean bean = getCMDConfig(getCMDNum(cmd));
        int newBuffId = 0;
        if (bean != null) {
            newBuffId = bean.getBuff();
        }
//        log.info("============buffId="+newBuffId+",num="+getCMDNum(cmd));
        cmd.setBuffId(newBuffId);

        //更新指挥队列BUFF
        for (Player p : players) {
            if (!(cmd.getCommanderId() == p.getId() || cmd.getRoleList().contains(p.getId()))) {
                continue;
            }
            if (oldBuffId > 0 && oldBuffId != newBuffId) {
                Manager.buffManager.deal().onRemoveBuff(p, oldBuffId);
            }
            Manager.buffManager.deal().onAddBuff(p, p, newBuffId);
        }

        sendCommandInfo(commander, cmd.getCommanderId(), cmd.getTargetId(), getCMDNum(cmd));

        if (synAll) {
            //通知对应阵营玩家
            noticeCampCMDInfo(map, players, cmd, platSid);
        } else {
            //通知指挥队列玩家信息变更
            noticeCMDInfo(map, players, cmd);
        }
    }

    private int getCMDNum(CommandData cmd) {
        return cmd.getRoleList().size() + (cmd.getCommanderId() > 0 ? 1 : 0);
    }

    private void enterCheckCommander(Player player, MapObject map, UniverseWarData uwData) {
        if (!uwData.isOpen()) {
            return;
        }

        ConcurrentHashMap<String, CommandData> commandDataMap = Manager.commandManager.getCommandDataMap().get(map.getId());
        if (commandDataMap == null) {
            commandDataMap = new ConcurrentHashMap<>();
            Manager.commandManager.getCommandDataMap().put(map.getId(), commandDataMap);
        }

        String platSid = player.playerCrossData.platSid;
        CommandData cmd = commandDataMap.get(platSid);
        if (cmd == null) {
            cmd = new CommandData();
            commandDataMap.put(platSid, cmd);
        }

        if (cmd.getCommanderId() <= 0) {
//            log.info("============没有指挥官，开始选择指挥官");
            dealCommander(map, map.getPlayers().values(), cmd, platSid, false, false);
            return;
        }
        sendCommandInfo(player, cmd.getCommanderId(), cmd.getTargetId(), getCMDNum(cmd));
    }

    private void noticeCMDInfo(MapObject map, Collection<Player> players, CommandData cmd) {
        CommandMessage.ResCommandInfo.Builder msg = CommandMessage.ResCommandInfo.newBuilder();
        CommandMessage.CommandInfo.Builder cmdBuilder = CommandMessage.CommandInfo.newBuilder();
        cmdBuilder.setRoleId(cmd.getCommanderId());
        cmdBuilder.setTargetId(cmd.getTargetId());
        cmdBuilder.setNum(getCMDNum(cmd));

        buildCMDInfo(map, cmd, cmdBuilder);

        msg.setInfo(cmdBuilder);
        for (Player p : players) {
            if (cmd.getRoleList().contains(p.getId())) {
//                log.info("----------------------------发送指挥消息,roleId:"+p.getId()+",commanderId="+cmd.getCommanderId()+",targetId="+cmd.getCommanderId()+",num="+(cmd.getRoleList().size()+1));
                MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    private void send_to_camp(Collection<Player> players, String platSid, int messId, String cmdName) {
        for (Player p : players) {
            if (!p.playerCrossData.platSid.equals(platSid)) {
                continue;
            }
            MessageUtils.notify_player(p, Notify.FIXED, messId, cmdName);
        }
    }

    private void noticeCampCMDInfo(MapObject map, Collection<Player> players, CommandData cmd, String platSid) {
        CommandMessage.ResCommandInfo.Builder msg = CommandMessage.ResCommandInfo.newBuilder();
        CommandMessage.CommandInfo.Builder cmdBuilder = CommandMessage.CommandInfo.newBuilder();
        cmdBuilder.setRoleId(cmd.getCommanderId());
        cmdBuilder.setTargetId(cmd.getTargetId());
        cmdBuilder.setNum(getCMDNum(cmd));

        buildCMDInfo(map, cmd, cmdBuilder);

        msg.setInfo(cmdBuilder);
        for (Player p : players) {
            if (!p.playerCrossData.platSid.equals(platSid)) {
                continue;
            }
            if (cmd.getCommanderId() == p.getId()) {
                continue;
            }
//            log.info("----------------------------发送指挥消息,roleId:"+p.getId()+",commanderId="+cmd.getCommanderId()+",targetId="+cmd.getCommanderId()+",num="+(cmd.getRoleList().size()+1));
            MessageUtils.send_to_player(p, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private void buildCMDInfo(MapObject map, CommandData cmd, CommandMessage.CommandInfo.Builder cmdBuilder) {
        Player player = map.getPlayer(cmd.getCommanderId());
        if (player != null) {
            cmdBuilder.setRoleName(player.getName());
            cmdBuilder.setRoleCareer(player.getCareer());
            cmdBuilder.setFightPower(player.getFightPoint());
            cmdBuilder.setGuildName(player.getGuildName());
//            cmdBuilder.setHeadId(0);
//            cmdBuilder.setHeadFrameId(0);

            cmdBuilder.setHead(MapUtils.getHead(player));

            CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(player);
            cmdBuilder.setFacade(fa);
        } else {
            cmdBuilder.setRoleName("");
            cmdBuilder.setRoleCareer(0);
            cmdBuilder.setFightPower(0);
            cmdBuilder.setGuildName("");
//            cmdBuilder.setHeadId(0);
//            cmdBuilder.setHeadFrameId(0);

            cmdBuilder.setHead(MapUtils.getDefaultHead());
            CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0, 0);
            cmdBuilder.setFacade(fa);
        }
    }

    private Player getPlayer(Collection<Player> players, long roleId) {
        for (Player player : players) {
            if (player.getId() == roleId) {
                return player;
            }
        }
        return null;
    }

    private void wearTitle(Player player) {
        int titleId = Global.Universe_Command_Title;
        Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(titleId);
        if (bean == null) {
            return;
        }

        TitleData data = player.getTitleData();
        if (!data.getTitleList().containsKey(titleId)) {
            data.getTitleList().put(titleId, bean.getTime());

            Map<Integer, Object> noticeMap = new HashMap<>();
            noticeMap.put(1, titleId);
            noticeMap.put(2, bean.getTime());

            TitleMessage.ResActiveTitleResult.Builder builder = TitleMessage.ResActiveTitleResult.newBuilder();
            TitleMessage.title.Builder title = TitleMessage.title.newBuilder();
            title.setId(titleId);
            title.setRemainTime(bean.getTime());
            builder.setInfo(title);
            MessageUtils.send_to_player(player, TitleMessage.ResActiveTitleResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            //同步游戏服
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.TitleActive, noticeMap, TitleMessage.ResActiveTitleResult.MsgID.eMsgID_VALUE, builder.build().toByteString());
        }

        Manager.titleManager.deal().onReqWearTitle(player, titleId);
    }

    @Override
    public boolean canOptTitle(Player player) {
        long mapId = player.getCurGps().getMapId();
        MapObject mapObject = Manager.mapManager.getMap(mapId);
        if (mapObject == null) {
            return true;
        }
        if (mapObject.getMapModelId() != 71001) {
            return true;
        }

        ConcurrentHashMap<String, CommandData> commandDataMap = Manager.commandManager.getCommandDataMap().get(mapId);
        if (commandDataMap == null) {
            return true;
        }

        CommandData cmd = commandDataMap.get(player.playerCrossData.platSid);
        if (cmd == null) {
            return true;
        }

        //指挥官无法操作称号
        if (player.getId() == cmd.getCommanderId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Universe_Command_Reward_Title_Notice);
            return false;
        }
        return true;
    }

    private void sendCommandInfo(Player player, long commanderId, long targetId, int num) {
        CommandMessage.ResCommandInfo.Builder msg = CommandMessage.ResCommandInfo.newBuilder();
        CommandMessage.CommandInfo.Builder cmdBuilder = getCMDBuilder(player, commanderId, targetId, num);

        msg.setInfo(cmdBuilder);
//        log.info("==============================发送指挥消息,roleId:"+player.getId()+",commanderId="+commanderId+",targetId="+targetId+",num="+num);
        MessageUtils.send_to_player(player, CommandMessage.ResCommandInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private CommandMessage.CommandInfo.Builder getCMDBuilder(Player player, long commanderId, long targetId, int num) {
        CommandMessage.CommandInfo.Builder cmdBuilder = CommandMessage.CommandInfo.newBuilder();
        cmdBuilder.setRoleId(commanderId);
        cmdBuilder.setTargetId(targetId);
        cmdBuilder.setNum(num);
        cmdBuilder.setRoleName(player.getName());
        cmdBuilder.setRoleCareer(player.getCareer());
        cmdBuilder.setFightPower(player.getFightPoint());
        cmdBuilder.setGuildName(player.getGuildName());
//        cmdBuilder.setHeadId(0);
//        cmdBuilder.setHeadFrameId(0);
        cmdBuilder.setHead(MapUtils.getHead(player));
        CommonMessage.FacadeAttribute.Builder fa = MapUtils.getFacade(player);
        cmdBuilder.setFacade(fa);
        return cmdBuilder;
    }

    private long findCommander(Collection<Player> players, Map<Integer, GuildBattleInfo> guildBattleInfoMap, String platSid) {
        Map<Integer, GuildBattleInfo> map = new TreeMap<>(guildBattleInfoMap);
        for (Map.Entry<Integer, GuildBattleInfo> entry : map.entrySet()) {
            long roleId = findRoleByGuild(players, entry.getValue(), platSid, entry.getKey());
            if (roleId > 0L) {
                return roleId;
            }
        }
        return 0L;
    }

    private long findRoleByGuild(Collection<Player> players, GuildBattleInfo gbi, String platSid, int rank) {
        List<Long> secIds = new ArrayList<>();
        for (Player player : players) {
            if (!player.playerCrossData.platSid.equals(platSid)) {
                continue;
            }

            if (rank == 1 && gbi.getSecMasterId().size() > 0) {//第一公会没有会长时才选副会长
                for (long rId : gbi.getSecMasterId()) {
                    if (player.getId() == rId) {
                        secIds.add(player.getId());
                    }
                }
            }

            if (player.getId() == gbi.getMasterId()) {//优先会长
                return player.getId();
            }
        }

        if (rank == 1 && secIds.size() > 0) {//随机选择一个副会长
            return secIds.get(RandomUtils.random(secIds.size()));
        }

        return 0L;
    }

    private long findHighestFightPower(Collection<Player> players, String platSid) {
        long roleId = 0L;
        long fightPower = 0L;
        for (Player player : players) {
            if (!player.playerCrossData.platSid.equals(platSid)) {
                continue;
            }

            if (roleId != player.getId() && player.getFightPoint() >= fightPower) {
                roleId = player.getId();
                fightPower = player.getFightPoint();
            }
        }
        return roleId;
    }

    /**
     * 刷新boss
     *
     * @param mapObject
     */
    private void refreshBoss(MapObject mapObject, int monsterModelId) {
        Cfg_Universe_boss_Bean bossBean = getCfgByMonsterModelId(monsterModelId);
        if (bossBean == null) {
            log.error(String.format("太虚战场刷新怪物{%s}失败!!!配置表Cfg_Universe_boss_Bean不存在！！！", monsterModelId));
            return;
        }
        Monster monster = MonsterManager.getInstance().createMonster(bossBean.getMonsterID());
        if (monster != null) {
            monster.changeLine(mapObject.getLineId());
            monster.changeMapId(mapObject.getId());
            monster.changeMapModelId(mapObject.getMapModelId());
            Position position = new Position();
            position.setX(bossBean.getPos().get(0).get(0));
            position.setY(bossBean.getPos().get(0).get(1));
            monster.setInitPos(position);
            monster.setCamp(5 * 100);
            Manager.mapManager.manager().onEnterMap(monster);
            MapParam.getUniverseWarData(mapObject).getRefreshMap().put(monster.getModelId(), TimeUtils.Time());
            //关注的怪物刷新
//            sendCareMonsterRefreshTip(bossid, bossBean);
        } else {
            log.error("太虚战场怪物刷新失败：monsterId=" + bossBean.getMonsterID());
            return;
        }
        Iterator<Integer> iterator = MapParam.getUniverseWarData(mapObject).getDieMap().keySet().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if (next == bossBean.getMonsterID()) {
                iterator.remove();
            }
        }

        MessageUtils.notify_Map(mapObject, Notify.FIXED, MessageString.UniverseMonsterRefresh);

        //更新玩家面板的怪物数据
        synMonsterUpdateAllMap(bossBean, mapObject);
    }

    /**
     * 全图同步怪物信息
     */
    private void synMonsterUpdateAllMap(Cfg_Universe_boss_Bean bossBean, MapObject mapObject) {
        MSG_UniverseMessage.ResUpdateMonsterRefresh.Builder msg = MSG_UniverseMessage.ResUpdateMonsterRefresh.newBuilder();
        MSG_UniverseMessage.UniverseMonsterInfo.Builder builder = bossToBossInfoBulild(bossBean, mapObject);
        msg.addMonsterInfos(builder);
        for (Player player : mapObject.getPlayers().values()) {
            int camp = getWarCamp(mapObject, player);
            if (bossBean.getCamp() != 5 && camp != bossBean.getCamp()) {//只发送玩家对应阵营（排除区域怪物）
                continue;
            }
            MessageUtils.send_to_player(player, MSG_UniverseMessage.ResUpdateMonsterRefresh.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void removeMap(MapObject map) {
        Manager.commandManager.getCommandDataMap().remove(map.getId());
    }

    @Override
    public void P2FReqUniverseWarPanel(MSG_UniverseMessage.P2FReqUniverseWarPanel messInfo) {
        MapObject map = Manager.mapManager.getMap(messInfo.getRoomID());
        if (map == null) {
            log.error("请求太虚战场面板数据时，地图对象没找到，roomId=" + messInfo.getRoomID());
            return;
        }
        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        Map<Integer, Long> dieMap = uwData.getDieMap();
        long roleId = messInfo.getRoleId();
        int anger = messInfo.getAnger();
        String key = messInfo.getPlatServerId();
        Player player = map.getPlayer(roleId);
        //玩家没有进入过副本
        if (player == null) {
            int camp = initCamp(uwData, key);
            String[] psi = key.split("_");
            int sid = Integer.parseInt(psi[1]);
            MSG_UniverseMessage.F2GResUniverseWarPanel.Builder msg = MSG_UniverseMessage.F2GResUniverseWarPanel.newBuilder();
            MSG_UniverseMessage.UniverseMonsterInfo.Builder bossInfo = MSG_UniverseMessage.UniverseMonsterInfo.newBuilder();
            Cfg_Universe_boss_Bean[] beans = Cfg_Universe_boss_Container.GetInstance().getValuees();
            for (Cfg_Universe_boss_Bean bean : beans) {
                //检查阵营
                if (bean.getCamp() != 5 && camp != bean.getCamp()) {//只发送玩家对应阵营（排除区域怪物）
                    continue;
                }
                ReadIntegerArray rs = bean.getWorldLevel();
                if (!isRange(uwData.getWorldLevel(), rs.get(0), rs.get(1))) {
                    continue;
                }

                bossInfo.setModelId(bean.getId());
                bossInfo.setType(bean.getType());
                bossInfo.setCare(false);
                //boss血量百分比
                if (bean.getGroup() == 1) {
                    Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(bean.getMonsterID());
                    Monster monster = getMonsterObject(map, bean.getMonsterID());
                    if (monster == null || monster.isDie()) {
                        msg.setCampBossHP(0);
                    } else {
                        msg.setCampBossHP(getPercent(monster.getCurHp(), monsterBean.getMaxHp()));
                    }
                } else if (bean.getGroup() == 2) {
                    Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(bean.getMonsterID());
                    Monster monster = getMonsterObject(map, bean.getMonsterID());
                    if (monster == null || monster.isDie()) {
                        msg.setFinalBossHP(0);
                    } else {
                        msg.setFinalBossHP(getPercent(monster.getCurHp(), monsterBean.getMaxHp()));
                    }
                }

                if (dieMap.keySet().contains(bean.getMonsterID())) {//在是死亡列表中 且他是 精英 或者领主 负数表示已经击杀
                    Long refreshTime = uwData.getRefreshMap().get(bean.getMonsterID());
                    if (refreshTime == null) {
                        bossInfo.setRefreshTime(-1);
                    } else {
                        bossInfo.setRefreshTime((int) ((refreshTime - TimeUtils.Time()) / 1000));
                    }
                } else {
                    bossInfo.setRefreshTime(0);//存活
                }
                msg.addMonsterInfos(bossInfo);
            }
            msg.setRoleId(roleId);
            msg.setAnger(anger);
            FightClientManager.GetInstance().send_to_game(sid, psi[0], MSG_UniverseMessage.F2GResUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        //在副本中打开面板
        sendF2GResUniverseWarPanel(player, map, uwData, anger);
    }

    private void sendF2GResUniverseWarPanel(Player player, MapObject map, UniverseWarData uwData, int anger) {
        MSG_UniverseMessage.F2GResUniverseWarPanel.Builder msg = MSG_UniverseMessage.F2GResUniverseWarPanel.newBuilder();
        Cfg_Universe_boss_Bean[] beans = Cfg_Universe_boss_Container.GetInstance().getValuees();
        for (Cfg_Universe_boss_Bean bean : beans) {
            //检查阵营
            int camp = getWarCamp(map, player);
            if (bean.getCamp() != 5 && camp != bean.getCamp()) {//只发送玩家对应阵营（排除区域怪物）
                continue;
            }
            ReadIntegerArray rs = bean.getWorldLevel();
            if (!isRange(uwData.getWorldLevel(), rs.get(0), rs.get(1))) {
                continue;
            }

            MSG_UniverseMessage.UniverseMonsterInfo.Builder bossInfo = MSG_UniverseMessage.UniverseMonsterInfo.newBuilder();
            bossInfo.setModelId(bean.getId());
            bossInfo.setType(bean.getType());
            if (player.getFollowedBossList().contains(bean.getId())) {
                bossInfo.setCare(true);
            } else {
                bossInfo.setCare(false);
            }
            //boss血量百分比
            if (bean.getGroup() == 1) {
                Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(bean.getMonsterID());
                Monster monster = getMonsterObject(map, bean.getMonsterID());
                if (monster == null || monster.isDie()) {
                    msg.setCampBossHP(0);
                } else {
                    msg.setCampBossHP(getPercent(monster.getCurHp(), monsterBean.getMaxHp()));
                }
            } else if (bean.getGroup() == 2) {
                Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(bean.getMonsterID());
                Monster monster = getMonsterObject(map, bean.getMonsterID());
                if (monster == null || monster.isDie()) {
                    msg.setFinalBossHP(0);
                } else {
                    msg.setFinalBossHP(getPercent(monster.getCurHp(), monsterBean.getMaxHp()));
                }
            }

            if (uwData.getDieMap().keySet().contains(bean.getMonsterID())) {//在是死亡列表中 且他是 精英 或者领主 负数表示已经击杀
                Long refreshTime = uwData.getRefreshMap().get(bean.getMonsterID());
                if (refreshTime == null) {
                    bossInfo.setRefreshTime(-1);
                } else {
                    bossInfo.setRefreshTime((int) ((refreshTime - TimeUtils.Time()) / 1000));
                }
            } else {
                bossInfo.setRefreshTime(0);//存活
            }
            msg.addMonsterInfos(bossInfo);
        }
        msg.setRoleId(player.getId());
        msg.setAnger(anger);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), MSG_UniverseMessage.F2GResUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private int initCamp(UniverseWarData uwData, String key) {
        if (!uwData.getCampMap().keySet().contains(key)) {
            uwData.getCampMap().put(key, uwData.getCampMap().size() + 1);
            return uwData.getCampMap().get(key);
        } else {
            return uwData.getCampMap().get(key);
        }
    }

    private int getPercent(long curHP, long MaxHp) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        return Integer.parseInt(numberFormat.format((float) curHP / (float) MaxHp * 100));
    }

    private Monster getMonsterObject(MapObject map, int monsterModelId) {
        for (Monster monster : map.getMonsters().values()) {
            if (monster.getModelId() == monsterModelId) {
                return monster;
            }
        }
        return null;
    }

    @Override
    public void F2GResUniverseWarPanel(MSG_UniverseMessage.F2GResUniverseWarPanel messInfo) {
        Player player = Manager.playerManager.getPlayer(messInfo.getRoleId());
        if (player == null) {
            return;
        }
        MSG_UniverseMessage.ResUniverseWarPanel.Builder msg = MSG_UniverseMessage.ResUniverseWarPanel.newBuilder();
        msg.setAnger(messInfo.getAnger());
        msg.setCampBossHP(messInfo.getCampBossHP());
        msg.setFinalBossHP(messInfo.getFinalBossHP());
        for (MSG_UniverseMessage.UniverseMonsterInfo bossInfo : messInfo.getMonsterInfosList()) {
            MSG_UniverseMessage.UniverseMonsterInfo.Builder monsterInfo = bossInfo.toBuilder();
            if (player.getFollowedBossList().contains(monsterInfo.getModelId())) {
                monsterInfo.setCare(true);
            } else {
                monsterInfo.setCare(false);
            }
            msg.addMonsterInfos(monsterInfo);
        }
        MessageUtils.send_to_player(player, MSG_UniverseMessage.ResUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void P2FOpenBlock(MSG_UniverseMessage.P2FOpenBlock messInfo) {
        if (!messInfo.getOpen()) {
            return;
        }
        //通知地图所有玩家阻挡打开
        MapObject map = Manager.mapManager.getMap(messInfo.getRoomID());
        if (map == null) {
            log.error("太虚战场改变阻挡状态时，地图对象没找到，roomId=" + messInfo.getRoomID());
            return;
        }

        UniverseWarData uwData = MapParam.getUniverseWarData(map);
        for (int i = 0; i < maxCamp; i++) {
            DynamicBlock door = map.getDoors().get(blocks[i][0]);
            if (null == door) {
                return;
            }
            door.setOpen(messInfo.getOpen());
        }
        log.error("太虚战场准备时间结束打开阻挡");
        if (uwData.getCampMap().isEmpty()) {//没有玩家进入副本
            return;
        }
        //通知对应阵营玩家对应的阻挡开启了
        for (Player player : map.getPlayers().values()) {
            if (player == null) {
                continue;
            }
            int camp = getWarCamp(map, player);
            String blockName = blocks[camp - 1][0];
            MapMessage.ResUpdateBlockDoor.Builder msg = MapMessage.ResUpdateBlockDoor.newBuilder();
            msg.setId(blockName);
            msg.setIsopen(messInfo.getOpen());
            MessageUtils.send_to_player(player, MapMessage.ResUpdateBlockDoor.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onReqCareMonster(Player player, MSG_UniverseMessage.ReqCareMonster messInfo) {
        int type = messInfo.getType();
        int monsterModelId = messInfo.getModelId();
        if (type == 1) {
            if (!player.getFollowedBossList().contains(monsterModelId)) {
                player.getFollowedBossList().add(monsterModelId);
            }
        } else {
            if (player.getFollowedBossList().contains(monsterModelId)) {
                player.getFollowedBossList().remove((Integer) monsterModelId);
            }
        }
        MSG_UniverseMessage.ResCareMonster.Builder msg = MSG_UniverseMessage.ResCareMonster.newBuilder();
        msg.setModelId(monsterModelId);
        msg.setType(type);
        MessageUtils.send_to_player(player, MSG_UniverseMessage.ResCareMonster.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqDamageRank(Player player, MSG_UniverseMessage.ReqDamageRank messInfo) {
        long monsterId = messInfo.getMonsterId();
        MapObject map = Manager.mapManager.getMap(player.getCurGps().getMapId());
        if (map == null) {
            log.error("地图对象没找到，mapId=" + player.getCurGps().getMapId());
            return;
        }
        if (map.getMonster(monsterId) == null) {
//            log.error("地图中没找到怪物，mapId=" + player.getCurGps().getMapId() + ",monsterId=" + monsterId);
            return;
        }
        sendDamageRank(player, map, map.getMonster(monsterId));
    }

    private void sendDamageRank(Player attacker, MapObject map, Monster monster) {
        int rank = 0;
        // 攻击者
        MSG_UniverseMessage.DamageInfo.Builder playerSelf = null;
        MSG_UniverseMessage.DamageRank.Builder playerRank = MSG_UniverseMessage.DamageRank.newBuilder();
        MSG_UniverseMessage.ResDamageRank.Builder msg = MSG_UniverseMessage.ResDamageRank.newBuilder();
        Map<String, Long> sDamageMap = new HashMap<>();
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p = (Player) hatred.getTarget();
            String tmpKey = String.valueOf(p.getCurServerId());
            if (sDamageMap.containsKey(tmpKey)) {
                sDamageMap.put(tmpKey, sDamageMap.get(tmpKey) + hatred.getHatred());
            } else {
                sDamageMap.put(tmpKey, hatred.getHatred());
            }
            int pCamp = getWarCamp(map, p);
            int aCamp = getWarCamp(map, attacker);
            if (pCamp != aCamp) {//其他阵营玩家
                continue;
            }
            rank++;
            if (rank <= maxRank) {
                MSG_UniverseMessage.DamageInfo.Builder harmMsg = MSG_UniverseMessage.DamageInfo.newBuilder();
                harmMsg.setRank(rank);
                harmMsg.setName(hatred.getTarget().getName());
                harmMsg.setDamage(hatred.getHatred());
                playerRank.addDamageList(harmMsg);
            }

            if (hatred.getTarget().getId() == attacker.getId()) {
                MSG_UniverseMessage.DamageInfo.Builder harmMsg = MSG_UniverseMessage.DamageInfo.newBuilder();
                harmMsg.setRank(rank > maxRank ? 0 : rank);
                harmMsg.setName(hatred.getTarget().getName());
                harmMsg.setDamage(hatred.getHatred());
                playerSelf = harmMsg;
                playerRank.setMyDamage(playerSelf);
            }
        }

        boolean sendServer = true;
        if (playerSelf == null) {//自己没有攻击
            MSG_UniverseMessage.DamageInfo.Builder harmMsg = MSG_UniverseMessage.DamageInfo.newBuilder();
            harmMsg.setRank(0);
            harmMsg.setName(attacker.getName());
            harmMsg.setDamage(0L);
            playerRank.setMyDamage(harmMsg);
            sendServer = false;
        }
        msg.setPlayerRank(playerRank);
        //发送服务器伤害排行
        if (sendServer) {
            TreeMap<Long, String> rankMap = new TreeMap<>(Comparator.comparingLong(n -> (long) n).reversed());
            MSG_UniverseMessage.DamageRank.Builder serverRank = MSG_UniverseMessage.DamageRank.newBuilder();
            for (Map.Entry<String, Long> entry : sDamageMap.entrySet()) {
                rankMap.put(entry.getValue(), entry.getKey());
            }

            String key = String.valueOf(attacker.getCurServerId());
            int sRank = 0;
            for (Map.Entry<Long, String> entry : rankMap.entrySet()) {
                MSG_UniverseMessage.DamageInfo.Builder harmMsg = MSG_UniverseMessage.DamageInfo.newBuilder();
                harmMsg.setRank(++sRank);
                harmMsg.setName(entry.getValue());
                harmMsg.setDamage(entry.getKey());
                serverRank.addDamageList(harmMsg);
                if (key.equals(entry.getValue())) {
                    serverRank.setMyDamage(harmMsg);
                }
            }
            msg.setServerRank(serverRank);
        }
        if (playerSelf != null) {//自己没有造成伤害则不返回消息
            MessageUtils.send_to_player(attacker, MSG_UniverseMessage.ResDamageRank.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void calcBossBirth() {

    }

    private void sendCommandBulletScreen(Player player, String context, boolean isSystem) {
        CommandMessage.ResCommandBulletScreen.Builder msg = CommandMessage.ResCommandBulletScreen.newBuilder();
        msg.setContext(context);
        if(isSystem){
            msg.setRoleId(0);
            msg.setRoleName("");
            msg.setRoleCareer(0);

            String[] tmp = context.split("@_@");
            msg.setContext(tmp[0]);
            if (tmp.length > 1) {
                for (int i = 1; i < tmp.length; ++i) {
                    msg.addParamLists(getParamStructBuilder(tmp[i]));
                }
            }
        }else{
            msg.setRoleId(player.getId());
            msg.setRoleName(player.getName());
            msg.setRoleCareer(player.getCareer());
        }
        MessageUtils.send_to_player(player, CommandMessage.ResCommandBulletScreen.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private mailMessage.paramStruct.Builder getParamStructBuilder(String value) {
        mailMessage.paramStruct.Builder info = mailMessage.paramStruct.newBuilder();
        if (value.length() < 3) {
            info.setMark(0);
            info.setParamsValue(value);
            return info;
        }
        char sr = value.charAt(1);
        char srs = value.charAt(2);

        if (sr == '&' && srs == '_') {
            String[] tt = value.split("&_");
            info.setMark(Integer.parseInt(tt[0]));
            info.setParamsValue(value.substring(3));
        } else {
            info.setMark(0);
            info.setParamsValue(value);
        }
        return info;
    }

    private void sendF2PBossRefreshTip(int groupId, int configId, int bossType) {
        CrossServerMessage.F2PBossRefreshTip.Builder msg = CrossServerMessage.F2PBossRefreshTip.newBuilder();
        msg.setGroupID(groupId);
        msg.setBossID(configId);
        msg.setType(bossType);
        MessageUtils.send_to_public(CrossServerMessage.F2PBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void sendCareMonsterRefreshTip(int modelId) {
        for (Player p : Manager.playerManager.getPlayersCache().values()) {
            if (!p.isOnline()){
                continue;
            }
            if (!p.getFollowedBossList().contains(modelId)) {
                continue;
            }
            int anger = (int) Manager.countManager.getCount(p, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
            if (anger >= Global.Universe_Anger_Limit) {
                continue;
            }
            Manager.bossManager.manager().sendBossRefresh(p, modelId, BossTypeConst.UNIVERSE_WAR_BOSS);
        }
    }

    @Override
    public void checkAnger(Player player) {
        long nowTime = TimeUtils.Time();
        long nextTime = findNearTime(player.getUniverseLastTime(), Global.UniverseNuqReset);
        if (nextTime > 0 && nowTime >= nextTime) {
            player.setUniverseLastTime(nowTime);
            Manager.countManager.setCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue(), Count.RefreshType.CountType_Forever, 0L);
            synAnger(player);
        }
    }

    private long findNearTime(long time, ReadIntegerArray times) {
        long todayBeginTime = TimeUtils.getTodayBeginTime();
        long nearTime = 0;
        for (int i = 0; i < times.getValue().length; i++) {
            Integer timeNode = times.getValue()[i];
            nearTime = todayBeginTime + timeNode * 60 * 1000;
            if (nearTime >= time) {
                return nearTime;
            }
        }
        return 0;
    }

    @Override
    public void synAnger(Player player) {
        MSG_UniverseMessage.ResSynAnger.Builder msg = MSG_UniverseMessage.ResSynAnger.newBuilder();
        msg.setAnger((int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue()));
        MessageUtils.send_to_player(player, MSG_UniverseMessage.ResSynAnger.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void load() {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.UniverseWarActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        int stage = (int) objects[0];
        String method = (String) objects[1];
        switch (method) {
            case "activityChangeCallBack":
                activityChangeCallBack(stage);
                break;
            default:
        }
        return null;
    }

    public void activityChangeCallBack(int stage) {
//        if (GameServer.getInstance().IsFightServer()) {
//            return;
//        }
//        if (TimeUtils.getOpenServerDay() >= Global.UniverseSeverOpenTime) {
//            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.UniverseActivityEnd);
//        }
    }

    /**
     * 计算副本复活点
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public Position doCreateRelivePosition(MapObject map, Player player) {
        return map.getRelives().get(MapParam.getUniverseWarData(map).getCampMap().get(player.playerCrossData.platSid) - 1);
    }
}
