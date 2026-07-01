package common.couplefight;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Marrybattlerobot_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.container.Cfg_Clone_map_Container;
import com.data.container.Cfg_Marrybattlerobot_Container;
import com.data.struct.ReadArray;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.copymap.structs.FightRoomState;
import com.game.couplefight.struct.CouplefightPlayer;
import com.game.couplefight.struct.FightTeam;
import com.game.manager.Manager;
import com.game.map.script.ICrossCloneScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerDefine;
import com.game.robot.ai.RobotAi;
import com.game.robot.script.IRobotFightScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 仙侣对决 地图 脚本
 * @Auther: gouzhongliang
 * @Date: 2021/7/7 15:40
 */
public class CouplefightMapScript implements ICrossCloneScript, IRobotFightScript {

    final Logger log = LogManager.getLogger(CouplefightMapScript.class);

    /**房间id*/
    private final Integer key_roomId = 1;
    /**战斗状态*/
    private final Integer key_status = 2;
    private final int status_prepare = 0;//准备
    private final int status_ready = 1;//准备好
    private final int status_start = 2;//开始九 零一 起玩 www.901 75.com
    private final int status_over = 3;//结束
    /**玩家*/
    private final Integer key_players = 3;
    /**准备次数*/
    private final Integer key_ready_times = 4;
    /**是否是机器人房间*/
    private final Integer key_robot = 5;
    /**机器人*/
    private final Integer key_robots = 6;
    /**机器人阵营*/
    private final Integer key_robot_camp = 7;
    /**战斗类型*/
    private final Integer key_type = 8;
    /**战斗回合*/
    private final Integer key_round = 9;
    /**准备地图id*/
    private final Integer key_prefid = 10;
    /**开始时间*/
    private final Integer key_startTime = 11;
    /**结束时间*/
    private final Integer key_endTime = 12;

    @Override
    public int getId() {
        return ScriptEnum.CouplefightMapScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        log.info("创建仙侣对决战斗副本");
        mapObject.setAutoRemove(false);
        mapObject.getParams().put(key_roomId, objects[0]);

        //是否有机器人
        boolean hasrobot = false;
        int robotCamp = 0;
        Map<Integer, FightTeam> players = new HashMap<>();

        Object obj = objects[1];
        if(obj instanceof List){
            List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) obj;
            for(CommonMessage.CrossAttribute param : crossList){
                long id = param.getValue();
                int type = param.getType();
                if(type != 0){
                    continue;
                }
                int camp = param.getParam1();
                String robot = param.getParam();//1机器人 0玩家
                if("1".equals(robot)){
                    hasrobot = true;
                    robotCamp = camp;
                }else if("0".equals(robot)){
                    FightTeam team = players.get(camp);
                    if(team == null){
                        team = new FightTeam(camp);
                        players.put(camp, team);
                    }
                    team.add(new CouplefightPlayer(id, camp, false));
                }
            }
        }else if(obj instanceof Map){
            Map<Integer, List<Player>> ps = (Map<Integer, List<Player>>) obj;
            int camp = 0;
            for(List<Player> plas : ps.values()){
                FightTeam pss = new FightTeam(camp);
                for(Player p: plas){
                    pss.add(new CouplefightPlayer(p.getId(), camp, false));
                }
                players.put(camp, pss);
                camp++;
            }
        }

        List<Robot> robots = new ArrayList<>(2);
        if(hasrobot){//创建机器人
            FightTeam robotTeam = players.get(robotCamp);
            if(robotTeam == null){
                robotTeam = new FightTeam(robotCamp);
                players.put(robotCamp, robotTeam);
            }

//            IRobotScript is = (IRobotScript) Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);
//            Robot robot1 = is.OnMakeByJJCConfig(100*10000);
//            robot1.setCamp(robotCamp);
//            robot1.changeCurPos(mapObject.getBriths().get(robotCamp));
//            robot1.changeMapId(mapObject.getId());
//            robot1.changeLine(mapObject.getLineId());
//            robot1.changeMapModelId(mapObject.getMapModelId());
//            Manager.mapManager.manager().onEnterMap(robot1);
//
//            rs.add(new CouplefightPlayer(robot1.getId(), robotCamp, true));
//
//            Robot robot2 = is.OnMakeByJJCConfig(101 * 10000);
//            robot2.setCamp(robotCamp);
//            robot2.changeCurPos(mapObject.getBriths().get(robotCamp));
//            robot2.changeMapId(mapObject.getId());
//            robot2.changeLine(mapObject.getLineId());
//            robot2.changeMapModelId(mapObject.getMapModelId());
//            Manager.mapManager.manager().onEnterMap(robot2);
//            rs.add(new CouplefightPlayer(robot2.getId(), robotCamp, true));

            List<Robot> rs = createCoupleRobots();
            for(Robot r : rs){
                r.setCamp(robotCamp);
                r.changeCurPos(mapObject.getBriths().get(robotCamp));
                r.changeMapId(mapObject.getId());
                r.changeLine(mapObject.getLineId());
                r.changeMapModelId(mapObject.getMapModelId());
                Manager.mapManager.manager().onEnterMap(r);
                robotTeam.add(new CouplefightPlayer(r.getId(), robotCamp, true));
                robots.add(r);
            }
        }

        //计算副本时间
        Cfg_Clone_map_Bean mapbean= Cfg_Clone_map_Container.GetInstance().getValueByKey(mapObject.getMapModelId());
        ;//准备时间
        //副本总时间
        long startTime = TimeUtils.Time() + mapbean.getEnter_time();
        long endTime = TimeUtils.Time() + mapbean.getExist_time() - 5000;
        mapObject.getParams().put(key_status, status_prepare);
        mapObject.getParams().put(key_players, players);
        mapObject.getParams().put(key_robots, robots);
        mapObject.getParams().put(key_ready_times, 0);
        mapObject.getParams().put(key_robot, hasrobot);
        mapObject.getParams().put(key_robot_camp, robotCamp);
        mapObject.getParams().put(key_startTime, startTime);
        mapObject.getParams().put(key_endTime, endTime);

        if(objects.length >= 5){
            long prefid = (long) objects[2];
            int type = (int) objects[3];
            int round = (int) objects[4];
            mapObject.getParams().put(key_prefid,prefid);
            mapObject.getParams().put(key_type, type);
            mapObject.getParams().put(key_round, round);
        }

        mapObject.addMapOnceScriptEventTimer(getId(), "fightStart", mapbean.getEnter_time());
        mapObject.addMapOnceScriptEventTimer(getId(), "fightEnd", mapbean.getExist_time());
    }

    private List<Robot> createCoupleRobots() {
        List<Robot> robots = new ArrayList<>();
        Cfg_Marrybattlerobot_Bean[] beans = Cfg_Marrybattlerobot_Container.GetInstance().getValuees();
        int size = beans.length;
        int r = RandomUtils.random(size);
        Cfg_Marrybattlerobot_Bean bean = beans[r];

        Robot robot1 = createRobot(bean);
        robots.add(robot1);

        Cfg_Marrybattlerobot_Bean bean2 = null;
        for(Cfg_Marrybattlerobot_Bean b : beans){
            if(b.getRobotID() != bean.getRobotID() && b.getGroup_ID() == bean.getGroup_ID()){
                bean2 = b;
            }
        }
        if(bean2 != null){
            Robot robot2 = createRobot(bean2);
            robots.add(robot2);
        }

        return robots;
    }

    private Robot createRobot(Cfg_Marrybattlerobot_Bean config) {

        Robot robot = new Robot();
        robot.setModelId(config.getRobotID());
        robot.setMakerId(config.getRobotID());

        StringBuilder name = new StringBuilder();
        name.append(config.getName());
        robot.setName(name.toString());
        robot.setCareer(config.getCareer());
        robot.setLevel(RandomUtils.random(200,300));
        robot.setFightState(0);
        robot.setWeaponId(config.getWeaponsEquipId());
        robot.setWingId(0);

        robot.setGuildId(0);
        robot.setGuildName("");
        robot.setGuildPost(0);
        robot.setCamp(0);
        robot.setTitle(70054);  //策划指定写死的
        robot.setHeight(5f);
        robot.setPicTitle(0);

        robot.setFashionBodyId(config.getClotheEquipId());
        robot.setFashionWeaponId(config.getWeaponsEquipId());
        robot.setGrade(1);
        // 复制属性
        BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
        AttributeUtils.init(config.getAttribute(), att);
        robot.getPlayerCalculators().put(PlayerAttributeType.BASE, att);
        //BUFF属性添加
        robot.getPlayerCalculators().put(PlayerAttributeType.BUFF, new BaseIntAttribute(AttributeType.ATTR_MAX));
        long maxHp = att.getAdditionValue(AttributeType.ATTR_MaxHp);
        att.calFinalAttackSpeed();
        att.calFinalMoveSpeed();
        att.cleanMaxHP();
        att.addMaxHP(maxHp);
        AttributeUtils.addAttribute(robot.getAttribute(), att);
        robot.getAttribute().cleanMaxHP();
        robot.getAttribute().addMaxHP(maxHp);
//        robot.setAttribute(att);
        robot.setCurHp(att.MaxHP());
        robot.setCurHp(robot.getAttribute().MaxHP());
        robot.getAttribute().calFinalMoveSpeed();
        robot.setHairB(0);
        robot.setHairG(0);
        robot.setHairR(0);
        robot.setHairFrameId(0);
        robot.setBuchenVfxId(0);
        robot.setBuchenVfxId(0);
        robot.setStifleFabaoId(0);
        robot.setSpiritId(0);
        robot.setStateLv(5);
        //计算战力
        robot.setFightPower(Manager.playerAttAttributeManager.deal().calcFightPower(robot.getAttribute()));
        // 初始化技能
        for (ReadArray sa : config.getSkill().getValuees()) {
            int skillId = (int) sa.get(0);
            int level = (int) sa.get(1);
            // 用技能等级设置机器人等级,可能不对.
            robot.setLevel(level);
            Cfg_Skill_Bean sc = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
            if (sc == null) {
                continue;
            }
            if (robot.getSkills().containsKey(skillId)) {
                continue;
            }
            Skill skill = new Skill();
            skill.setSkillId(skillId);
            skill.setLevel(level);
            robot.getSkills().put(skill.getSkillId(), skill);
            robot.getSkillIds().add(skillId);
        }
        return robot;
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {
        log.info("进入仙侣对决战斗 map={} player={}", mapObject.getId(), player);
        Map<Integer,FightTeam> players = mapObject.getParam(key_players);
        int total = 0;
        boolean watching = true;
        int status = mapObject.getParam(key_status);
        for(FightTeam ps : players.values()){
            for(CouplefightPlayer p : ps.getPlayers()){
                total++;
                if(p.getId() == player.getId()){
                    player.setCamp(p.getCamp(), true);
                    watching = false;
                    if(status == status_start){
                        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateCamp, true);//阵营模式
                    }
                }
            }
        }

        if(watching){
            //观战处理

            return;
        }

        //是否是机器人房间
        boolean hasrobot = mapObject.getParam(key_robot);

        //发送进入副本消息
        CouplefightMessage.ResEnterFightMap.Builder res = CouplefightMessage.ResEnterFightMap.newBuilder();
        for(FightTeam ps : players.values()){
            for(CouplefightPlayer p : ps.getPlayers()){
                CouplefightMessage.FightPlayer.Builder fp = CouplefightMessage.FightPlayer.newBuilder();
                fp.setCamp(p.getCamp());
                fp.setId(p.getId());
                res.addPlayer(fp);
            }
        }
        res.setStartTime(mapObject.getParam(key_startTime));
        res.setEndTime(mapObject.getParam(key_endTime));
        MessageUtils.send_to_player(player, CouplefightMessage.ResEnterFightMap.MsgID.eMsgID_VALUE, res.build().toByteArray());

//        if(!hasrobot){
//            if(mapObject.getPlayers().size() >= total){
//                //准备好
//                mapObject.getParams().put(key_status, status_ready);
//                mapObject.addMapLoopScriptEventTimer(getId(), "readyGo", 3, 3000, 1000);
//                log.info("仙侣对决 战斗 准备完毕 map={} player={}", mapObject.getId());
//            }
//        }else{
//            if(mapObject.getPlayers().size() >= 2 && mapObject.getRobots().size() >= 2){
//                //准备好
//                mapObject.getParams().put(key_status, status_ready);
//                mapObject.addMapLoopScriptEventTimer(getId(), "readyGo", 3, 3000, 1000);
//                log.info("仙侣对决 战斗 准备完毕 map={} player={}", mapObject.getId());
//            }
//        }

    }

    private void readyGo(MapObject map) {
        int ready = map.getParam(key_ready_times);
        map.getParams().put(key_ready_times, ready + 1);

//        logger.info("仙侣对决战斗 Ready={}", ready);

        if (ready == 0) {
//            sendBattleInfo(map, ready);
            log.info("仙侣对决战斗 Ready map={}", map);
            return;
        }
        if (ready + 1 == 3) {
            for(Player p : map.getPlayers().values()){
                Manager.playerManager.manager().onUpdatePkState(p, PlayerDefine.PkStateTeam, true);//阵营模式
            }
            for(Robot robot : map.getRobots().values()){
                robot.setAi(RobotAi.JJC);
            }
            log.info("仙侣对决战斗 开始战斗 map={}", map);
            Map<Integer, FightTeam> players = map.getParam(key_players);
            if(players.size() == 1){//只有一个阵营的玩家，直接结束

//                finish(map,);
            }
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTING);
    }

    private void fightStart(MapObject map) {
//        int ready = map.getParam(key_ready_times);
//        map.getParams().put(key_ready_times, ready + 1);

//        logger.info("仙侣对决战斗 Ready={}", ready);

        map.getParams().put(key_status, status_start);

        Manager.mapManager.setBlockDoor(map, "DynamicBlocker", true);
        Manager.mapManager.setBlockDoor(map, "DynamicBlocker1", true);

        for(Player p : map.getPlayers().values()){
            Manager.playerManager.manager().onUpdatePkState(p, PlayerDefine.PkStateCamp, true);//阵营模式
        }
        int robotCamp = map.getParam(key_robot_camp);
        for(Robot robot : map.getRobots().values()){
            robot.setAi(RobotAi.JJC);
            robot.setCamp(robotCamp, true);
        }
        log.info("仙侣对决战斗 开始战斗 map={}", map);
        Map<Integer,FightTeam> players = map.getParam(key_players);
        if(players.size() == 1){//只有一个阵营的玩家，直接结束

//          finish(map,);
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHTING);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
//        CrossServerMessage.F2PPlayerOutFightRoom.Builder req = CrossServerMessage.F2PPlayerOutFightRoom.newBuilder();
//        long fid = map.getParam(key_roomId);
//        req.setFightId(fid);
//        req.setRoleId(player.getId());
//        req.setModelId(map.getMapModelId());
//        MessageUtils.send_to_public(CrossServerMessage.F2PPlayerOutFightRoom.MsgID.eMsgID_VALUE, req.build().toByteArray());
        log.info("仙侣对决战斗 玩家退出副本"+ player.toString());
        onDie(map, player.getId());
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        onDie(map, player.getId());
    }

    private void onDie(MapObject map, long pid) {
        Map<Integer, FightTeam> players = map.getParam(key_players);

        //玩家都是一个阵营
        FightTeam win = null;
        FightTeam lose = null;
        for(FightTeam ps : players.values()){
            boolean campAllDead = true;
            for(CouplefightPlayer p : ps.getPlayers()){
                if(p.getId() == pid){
                    p.setDead(true);
                }
                if(!p.getDead()){
                    campAllDead = false;
                }
            }
            if(campAllDead){
                lose = ps;
            }else{
                win = ps;
            }
        }

        if(lose != null){
            finish(map, win, lose);
        }
    }

    /**
     * 结束
     * @param map
     */
    private void finish(MapObject map) {
        int status = map.getParam(key_status);
        if(status == status_over){
            return;
        }
        //计算出胜利和失败者
        Map<Integer, FightTeam> players = map.getParam(key_players);
        FightTeam t1 = null;
        FightTeam t2 = null;
        for(FightTeam ps : players.values()){
            if(t1 == null){
                t1 = ps;
            }else{
                t2 = ps;
            }
            //总血量百分比
            double hpPercent = 0;
            //总战斗力
            long fight = 0;
            //总等级
            int level = 0;
            for(CouplefightPlayer player : ps.getPlayers()){
                if(player.isRobot()){
                    Robot r = map.getRobots().get(player.getId());
                    if(r != null){
                        player.setHpPercent(r.getCurHp() + 0.0 / r.getAttribute().MaxHP());
                        player.setFightPower(r.getFightPower());
                        player.setLevel(r.getLevel());
                    }else{
                        log.error("机器人不在副本");
                    }
                }else{
                    Player p = map.getPlayers().get(player.getId());
                    if(p != null){
                        player.setHpPercent(p.getCurHp() + 0.0 / p.getAttribute().MaxHP());
                        player.setFightPower(p.getFightPoint());
                        player.setLevel(p.getLevel());
                    }else{
                        log.error("玩家不在副本");
                    }
                }
                hpPercent += player.getHpPercent();
                fight += player.getFightPower();
                level += player.getLevel();
            }
            ps.setLevel(level);
            ps.setFight(fight);
            ps.setHpPercent(hpPercent);
        }
        //胜负判决
        FightTeam win = null; FightTeam lose = null;
        if(t1.getHpPercent() > t2.getHpPercent()){
            win = t1;
            lose = t2;
        }else if(t1.getHpPercent() < t2.getHpPercent()){
            win = t2;
            lose = t1;
        }else if(t1.getFight() > t2.getFight()){
            win = t1;
            lose = t2;
        }else if(t1.getFight() < t2.getFight()){
            win = t2;
            lose = t1;
        }else if(t1.getLevel() > t2.getLevel()){
            win = t1;
            lose = t2;
        }else if(t1.getLevel() < t2.getLevel()){
            win = t2;
            lose = t1;
        }else{
            int r = RandomUtils.random(100);
            if(r % 2 == 0){
                win = t1;
                lose = t2;
            }else{
                win = t2;
                lose = t1;
            }
        }
        finish(map, win, lose);
    }

    /**
     * 战斗结束
     */
    private void finish(MapObject map, FightTeam win, FightTeam lose) {
//        map.setStop(true);
//        map.setAutoRemove(false);
        int status = map.getParam(key_status);
        if(status == status_over){
            return;
        }
        map.getParams().put(key_status, status_over);
        log.info("仙侣对决战斗对局结束 win:{} lose:{}", win, lose);

        //取消机器人AI
        for(Robot r : map.getRobots().values()){
            r.setAi(null);
        }

        CouplefightMessage.F2PResFightResult.Builder res = CouplefightMessage.F2PResFightResult.newBuilder();
        for(CouplefightPlayer p : win.getPlayers()){
            p.setWin(true);
            if(!p.isRobot()){
                res.addWin(p.getId());
                Player o = map.getPlayer(p.getId());
                if(o != null){
                    Manager.playerManager.manager().onUpdatePkState(o, PlayerDefine.PkStatePeace, true);//切换和平模式
                    //加分
                    log.info("胜利 player:" + o);
                }
            }
        }

        for(CouplefightPlayer p : lose.getPlayers()){
            p.setWin(false);
            if(!p.isRobot()){
                res.addLose(p.getId());
                Player o = map.getPlayer(p.getId());
                if(o != null){
                    Manager.playerManager.manager().onUpdatePkState(o, PlayerDefine.PkStatePeace, true);//切换和平模式
                    //加分
                    log.info("失败 player:" + o);
                }
            }
        }

        long fid = map.getParam(key_roomId);
        res.setFid(fid);

        Integer type = map.getParam(key_type);
        res.setType(type == null ? 1 : type);
        //发送结束消息
        MessageUtils.send_to_public(CouplefightMessage.F2PResFightResult.MsgID.eMsgID_VALUE, res.build().toByteArray());

        //关闭
        map.addMapOnceScriptEventTimer(getId(), "close", 3000);
    }

    /**
     * 关闭
     *
     * @param mapObject
     */
    private void close(MapObject mapObject) {
        log.info("关闭仙侣对决 战斗副本");
        //玩家
        List<Player> palyers = new ArrayList<>(mapObject.getPlayers().values());
        //
        Long prefid = mapObject.getParam(key_prefid);
        Integer type = mapObject.getParam(key_type);
        Integer round = mapObject.getParam(key_round);
        Map<Integer, FightTeam> playerInfos = mapObject.getParam(key_players);
        MapObject preMap = null;
        if(prefid != null){
            preMap = Manager.mapManager.getMap(prefid);
        }
        for (Player player : palyers) {
            log.info("仙侣对决 战斗副本 player exit:{}", player);
//            Manager.copyMapManager.outZone(player);
//            Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, player.playerCrossData.toFightId, player.playerCrossData.toZoneModelId);
            if(prefid != null){
                //判断是退出地图还是返回准备地图
                if(type > 2){//冠军赛，输一局出局
                    boolean win = isWin(playerInfos, player);
                    if(win && preMap != null){
                        Manager.mapManager.changeMap(player, preMap.getId(), preMap.getBriths().get(0), false);
                    }else{
                        Manager.copyMapManager.manager().onReqCopyMapOut(player);
                    }
                }else if(type == 2){
                    if(round < 9 && preMap != null){
                        Manager.mapManager.changeMap(player, preMap.getId(), preMap.getBriths().get(0), false);
                    }else{
                        Manager.copyMapManager.manager().onReqCopyMapOut(player);
                    }
                }
            }else{
                Manager.copyMapManager.manager().onReqCopyMapOut(player);
            }
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
        mapObject.setStop(true);
        mapObject.setAutoRemove(true);
    }

    /**
     * 玩家是输还是赢
     * @param playerInfos
     * @param player
     * @return
     */
    private boolean isWin(Map<Integer, FightTeam> playerInfos, Player player) {
        for(FightTeam ps : playerInfos.values()){
            for(CouplefightPlayer p : ps.getPlayers()){
                if(p.getId() == player.getId()){
                    return p.isWin();
                }
            }
        }
        return true;
    }


    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "timeOut":
                timeOut(map);
                break;
            case "readyGo":
                readyGo(map);
                break;
            case "close":
                close(map);
                break;
            case "fightStart":
                fightStart(map);
                break;
            case "fightEnd":
                finish(map);
                break;
            default:
        }
    }

    private void timeOut(MapObject map) {
    }

    @Override
    public void removeMap(MapObject map) {
        int status = map.getParam(key_status);
        if(status == status_over){
            return;
        }
        finish(map);
    }

    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

    }

    @Override
    public void OnRobotDie(MapObject map, Robot robot, Fighter attacker) {
        onDie(map, robot.getId());
    }
}
