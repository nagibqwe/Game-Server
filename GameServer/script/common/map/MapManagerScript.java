package common.map;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.structs.ZoneCache;
import com.game.manager.Manager;
import com.game.map.command.CreateMapCommand;
import com.game.map.manager.MapGpsUtil;
import com.game.map.manager.MapManager;
import com.game.map.manager.MapsConfigManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.script.IMapManagerScript;
import com.game.map.structs.*;
import com.game.map.timer.MapHeartTimer;
import com.game.map.timer.MapSavePlayerTimer;
import com.game.monster.structs.Monster;
import com.game.monster.timer.MonsterBehaviorTimer;
import com.game.nature.structs.HuaxinEntity;
import com.game.npc.structs.Npc;
import com.game.npc.structs.Tombstone;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.player.timer.PlayerBehaviorTimer;
import com.game.player.timer.PlayerHeartTimer;
import com.game.robot.struct.Robot;
import com.game.robot.timer.RobotBehaviorTimer;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.server.impl.MapServerGroup;
import com.game.skill.structs.SkillMagic;
import com.game.structs.AttributeType;
import com.game.structs.EntityState;
import com.game.structs.Gather;
import com.game.utils.MessageInterFace;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MapMessage;
import game.message.MapMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


/**
 * 地图的公用接口处理类
 *
 * @author lw
 */
public class MapManagerScript implements IMapManagerScript, IScript {

    private static final Logger logger = LogManager.getLogger("MapManagerScript");

    final int Hide_Model_0 = 0;
    final int Hide_Model_1 = 1; //隐藏宠物
    final int Hide_Model_2 = 2;

    @Override
    public MapObject createCopyMap(int cloneId, int level, long ownId, Object... objects) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneId);
        if (bean == null) {
            logger.info("副本不存在:" + cloneId);
            return null;
        }

        Cfg_Mapsetting_Bean mapSet = CfgManager.getCfg_Mapsetting_Container().getValueByKey(bean.getMapid());
        if (null == mapSet || mapSet.getType() == MapDefine.WORLD_MAP) {
            logger.info("错误的地图类型 mapID" + bean.getMapid());
            return null;
        }
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(bean.getMapid());
        if (null == mapCfg) {
            logger.error("初始化地图失败mapCfg == null, mapID" + bean.getMapid());
            return null;
        }

        long mapId;
        if (GameServer.getInstance().IsFightServer()) {
            mapId = (Long) objects[0];
        } else {
            mapId = IDConfigUtil.getLogId();
        }

        MapObject map = initMap(bean.getMapid(), mapId, 0, objects);
        map.setZoneModelId(cloneId);
        map.setZone(new ZoneCache(cloneId, level));
        map.setOwnId(ownId);
        if (bean.getExist_time() != 0) {
            map.setDelTime(TimeUtils.Time() + bean.getEnter_time() + bean.getExist_time() + 1000 * 30);
        }
        Manager.mapManager.addCommand(new CreateMapCommand(map, objects));
        return map;
    }

    @Override
    public MapObject createWorldMap(Cfg_Mapsetting_Bean bean, int line) {

        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(bean.getMap_id());
        if (null == mapCfg) {
            logger.error("初始化地图失败mapCfg == null, mapID" + bean.getMap_id());
            return null;
        }

        long mapId = IDConfigUtil.getLogId();
        MapObject mapObject = initMap(bean.getMap_id(), mapId, line);
        ArrayList<MapObject> mapObjects = Manager.mapManager.getWorldMaps().get(bean.getMap_id());
        if (mapObjects == null) {
            mapObjects = new ArrayList<>();
            mapObjects.add(mapObject);
            Manager.mapManager.getWorldMaps().put(bean.getMap_id(), mapObjects);
        } else {
            mapObjects.add(mapObject);
        }
        createMap(mapObject, new Object[1]);
        return mapObject;
    }

    @Override
    public void onEnterMap(IMapObject play) {
        if (play instanceof Player) {
            enter((Player) play);
            return;
        }
        if (play instanceof Monster) {
            enter((Monster) play);
            return;
        }
        if (play instanceof Pet) {
            enter((Pet) play);
            return;
        }
        if (play instanceof Gather) {
            enter((Gather) play);
            return;
        }

        if (play instanceof SkillMagic) {
            enter((SkillMagic) play);
            return;
        }
        if (play instanceof Npc) {
            enter((Npc) play);
            return;
        }
        if (play instanceof Robot) {
            enter((Robot) play);
        }
        if (play instanceof Tombstone) {
            enter((Tombstone) play);
        }
        if (play instanceof GroundBuff) {
            enter((GroundBuff) play);
        }
    }

    @Override
    public void onEnterMap(Player player, MapObject map, Position pos) {
        try {
            if (map == null) {
                logger.error(player.getName() + "(" + player.getId() + ")发起进入地图时,地图为空" + player.getCurGps().toString(), new NullPointerException());
                return;
            }

            Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
            if (bean == null) {
                return;
            }

            //进入地图之前 设置恢复默认阵营
            if (GameServer.getInstance().IsFightServer()) {
                player.setCamp(player.playerCrossData.fightCampNo, true);
            } else {
                player.setCamp(0, true);
            }

            logger.info(player.getName() + "(" + player.getId() + ")正常进入地图：" + map.getMapModelId() + " , mapId" + map.getId() + " old pos=" + player.getOld().getPos());
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.AttackMove);
            MapUtils.sendFightState(player);

            player.setInBattle(false);
            player.clearHatred();
            player.setCurAttackTargetId(0);

            if (!Utils.isCanMove(map, pos)) {
                pos = map.getBrithPos();
                if (1 <= (map.getMapModelId() - 62000) && (map.getMapModelId() - 62000) <= 9) {
                    pos = map.getRandomRelivePos();
                    logger.info("勇者巅峰进入坐标：" + map.getMapModelId() + ":" + pos);
                }
                if (null == pos) {
                    logger.error(player + "玩家企图进入阻挡点！由于地图没有出生点，修正失败");
                    return;
                }
                logger.error(player + "玩家企图进入阻挡点！ pos" + player.gainCurPos() + ",重置为pos=" + pos);
                player.changeCurPos(pos);
            }

            //附加地图基本信息
            map.addPlayer(player.getId(), player);
            player.changeMapId(map.getId());
            player.changeMapModelId(map.getMapModelId());
            player.changeLine(map.getLineId());
            player.changeCurPos(pos);

            player.removeSate(EntityState.ExitGame);
            player.addState(EntityState.Stand);
            player.setIsOnline((byte) 1);//

            //进入区域
            Area area = Manager.mapManager.getArea(pos, map);
            area.addPlayer(player.getId(), player);

            Pet pet = Manager.petManager.getBattlePet(player);
            if (pet != null) {
                pet.clearHatred();
                pet.setCurAttackTargetId(0);
                pet.setCurSlowSkill(null);
                MapGpsUtil.CopyGPS(player.getCurGps(), pet.getCurGps());
                Manager.mapManager.manager().onEnterMap(pet);
            }
            //法宝跟随
            HuaxinEntity huaxinEntity = player.getCurHuaxinEntity();
            if (huaxinEntity != null && huaxinEntity.getExcelId() > 0) {
                huaxinEntity.clearHatred();
                huaxinEntity.setCurAttackTargetId(0);
                huaxinEntity.setCurSlowSkill(null);
                MapGpsUtil.CopyGPS(player.getCurGps(), huaxinEntity.getCurGps());
                Manager.mapManager.manager().onEnterMap(huaxinEntity);
            }
            //同步周围对象
            onRefreshPos(player, map, pos);
            //阻挡们
            synBlockDoor(map, player);

            player.onHpChange(player);

            MapUtils.sendWakanChange(player);

            //更新队伍信息
            Manager.teamManager.deal().updateHpAndMapKey(player);

            //检查玩家的组队BUFF
            Manager.teamManager.deal().checkTeamBuff(player);

            //强转模式
            Manager.playerManager.manager().onUpdatePkState(player, bean.getFight_type(), true);

            //过地图清理BUFF
            try {
                Manager.buffManager.deal().changeMapClear(player);
            } catch (Exception e) {
                logger.error(e, e);
            }

            if (player.isDie()) {
                Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, ReliveType.Gm, false, player.gainCurPos());
            }
            if (map.getSetting().getIsscript() > 0) {
                boolean login = EntityState.ReConnect.compare(player.getState()) || EntityState.LoginGame.compare(player.getState());
                Manager.mapManager.base(map.getSetting().getIsscript()).onEnterMap(player, map, login );
            }

            //延迟300，策划方新宇喊过地图 灵力重置为300秒
            Manager.cooldownManager.addCooldown(player, CooldownTypes.LINGLI_CD, String.valueOf(AttributeType.ATTR_Wakan_Dec), 300);

            Manager.worldHelpManager.getScript().enterMap(player, map);
        } catch (Exception e) {
            String str = "";
            int size = e.getStackTrace().length;
            if (size < 2) {
                str = Arrays.toString(e.getStackTrace());
            } else {
                str = e.getStackTrace()[1].toString();
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("Enter_Map", str);
            logger.error(e, e);
        }
    }

    @Override
    public void onQuitMap(MapObject map, IMapObject paly, boolean isBroadcast) {
        if (paly instanceof Player) {
            quit(map, (Player) paly, isBroadcast);
            return;
        }
        if (paly instanceof Monster) {
            quit(map, (Monster) paly, isBroadcast);
            return;
        }
        if (paly instanceof Pet) {
            quit(map, (Pet) paly);
            return;
        }
        if (paly instanceof Gather) {
            quit(map, (Gather) paly);
            return;
        }

        if (paly instanceof SkillMagic) {
            quit(map, (SkillMagic) paly);
            return;
        }
        if (paly instanceof Npc) {
            quit(map, (Npc) paly);
            return;
        }

        if (paly instanceof Tombstone) {
            quit(map, (Tombstone) paly);
        }

        if (paly instanceof GroundBuff) {
            quit(map, (GroundBuff) paly);
        }

        if (paly instanceof Robot) {
            quit(map, (Robot) paly);
        }
    }

    @Override
    public void onCrossOutMap(Player player) {
        Manager.buffManager.deal().onDie(player);//清理战斗BUFF及死亡时不生效的BUFF
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        if (player.getRoads() != null) {
            player.clearRoads();
        }
        Pet pet = Manager.petManager.getBattlePet(player);
        if (null != pet) {
            onQuitMap(map, pet, true);
        }

        HuaxinEntity entity = player.getCurHuaxinEntity();
        if (null != entity) {
            onQuitMap(map, entity, true);
        }
        if (null != player) {
            onQuitMap(map, player, true);
        }
        // 清除数据
        map.removePlayer(player.getId());

        Area area = Manager.mapManager.getArea(player.gainCurPos(), map);
        if (area != null) {
            area.removePlayer(player.getId());
        } else {
            for (Area area2 : map.getAreas().values()) {
                if (area2.containPlayer(player.getId())) {
                    area2.removePlayer(player.getId());
                }
            }
        }

        //通知周围玩家，我离开
        MapMessage.ResPlayerDisappear.Builder msg = MapMessage.ResPlayerDisappear.newBuilder();
        msg.addPlayerIds(player.getId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);

        logger.info("玩家(" + player.getId() + ")退出地图(modelId_" + player.gainMapModelId() + " line_" + player.gainLine() + " 地图总人数：" + map.getPlayers().size());
    }

    private void synBlockDoor(MapObject map, Player player) {
        if (map.getDoors().size() <= 0) {
            return;
        }

        MapMessage.ResBlockDoors.Builder msg = MapMessage.ResBlockDoors.newBuilder();
        Iterator<DynamicBlock> iter = map.getDoors().values().iterator();
        while (iter.hasNext()) {
            DynamicBlock door = iter.next();
            MapMessage.BlockDoor.Builder mDoor = MapMessage.BlockDoor.newBuilder();
            mDoor.setId(door.getKey());
            mDoor.setIsopen(door.isOpen());
            msg.addDoors(mDoor);
        }
        MessageUtils.send_to_player(player, MapMessage.ResBlockDoors.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void SynMeToOther(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        //是否是分级发送地图
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config == null) {
            return;
        }
        boolean isfilter = (config.getFilter() == 1);
        //把我同步给其他player
        MapMessage.ResMapPlayer.Builder mapPlayer = MapMessage.ResMapPlayer.newBuilder();
        mapPlayer.setPlayer(MapUtils.getPlayerInfo(player));
        List<Area> areas = Manager.mapManager.getRounds(map, player.gainCurPos());
        Iterator<Area> areaIter = areas.iterator();
        Set<Player> others = new HashSet<>();
        while (areaIter.hasNext()) {
            Area area = areaIter.next();
            //周围玩家
            for (Player other : area.getPlayers()) {
//                if (other.getId() == player.getId()) {//自己不屏蔽， 用于刷新之用
//                    continue;
//                }
                if (isfilter) {
                    if (other.getUnfilters().contains(player.getId())) {
                        others.add(other);
                    }
                } else {
                    others.add(other);
                }
            }
        }
        //同步我给other
        MessageUtils.send_to_players(others, MapMessage.ResMapPlayer.MsgID.eMsgID_VALUE, mapPlayer.build().toByteArray(), map.getId());
    }

    private void onRefreshPos(Player player, MapObject map, Position pos) {

        //是否是分级发送地图
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config == null) {
            return;
        }
        boolean isfilter = (config.getFilter() == 1);
        if (isfilter) {
            try {
                IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.MessageBaseScript);
                MessageInterFace mScript = (MessageInterFace) script;
                mScript.OnCalcFilterR(player);

            } catch (Exception e) {
            }
        }

        //把其他对象同步给我
        MapMessage.ResRoundObjs.Builder roundObjs = MapMessage.ResRoundObjs.newBuilder();

        List<Area> areas = Manager.mapManager.getRounds(map, pos);
        OnEnterAreas(player, areas, isfilter, roundObjs);
        MessageUtils.send_to_player(player, MapMessage.ResRoundObjs.MsgID.eMsgID_VALUE, roundObjs.build().toByteArray());
    }


    @Override
    public int getId() {
        return ScriptEnum.MapManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnChangePos(Player player, Position oldPos) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            return;
        }
//        logger.error("旧区域areaId=" + Manager.mapManager.getAreaId(map, oldPos) + "当前区域areaId=" + Manager.mapManager.getAreaId(map, player.gainCurPos()) + player);

        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        boolean isfilter = (config.getFilter() == 1);

        List<Area> oldAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(map, oldPos));

        List<Area> newAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(player));

        //公共区域
        List<Area> publicAreas = new ArrayList<>();

        Iterator<Area> oldIter = oldAreas.iterator();
        while (oldIter.hasNext()) {
            Area area = oldIter.next();
            if (newAreas.contains(area)) {
                publicAreas.add(area);
                oldIter.remove();
            }
        }
        Iterator<Area> newIter = newAreas.iterator();
        while (newIter.hasNext()) {
            Area area = newIter.next();
            if (publicAreas.contains(area)) {
                newIter.remove();
            }
        }

        //周围信息
        MapMessage.ResRoundObjs.Builder msg = MapMessage.ResRoundObjs.newBuilder();

        OnQuitAreas(player, oldAreas, isfilter, msg);
        OnEnterAreas(player, newAreas, isfilter, msg);
        OnCheckPublicAreas(player, publicAreas, isfilter, msg);

        //如果改变量为空不广播
        if (msg.getMagicsCount() == 0
                && msg.getRemoveIdsCount() == 0
                && msg.getPlayersCount() == 0
                && msg.getMonstersCount() == 0
                && msg.getPetsCount() == 0
                && msg.getNpcsCount() == 0
                && msg.getGathersCount() == 0
                && msg.getBonfiresCount() == 0
                && msg.getTombstoneCount() == 0
                && msg.getGroundBuffCount() == 0) {
            //LOGGER.error("消息为空");
            return;
        }

        MessageUtils.send_to_player(player, MapMessage.ResRoundObjs.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //检测公共区域
    private void OnCheckPublicAreas(Player player, List<Area> areas, boolean isfilter, MapMessage.ResRoundObjs.Builder msg) {
        //fixbug消息分级检测公共区域
        if (!isfilter) {
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            return;
        }

        List<Player> others = new ArrayList<>();
        for (Area area : areas) {
            for (Player other : area.getPlayers()) {
                if (!other.canSee(player)) {
                    continue;
                }

                if (!other.getUnfilters().contains(player.getId())) {
                    continue;
                }
                others.add(other);
                msg.addPlayers(MapUtils.getPlayerInfo(other));
                if (map.getSetting().getHide_mode() == Hide_Model_1) {
                    continue;
                }
                Pet pet = Manager.petManager.getBattlePet(other);
                if (pet == null) {
                    continue;
                }
                msg.addPets(MapUtils.getPetInfo(pet));
            }
        }

        if (others.isEmpty()) {
            return;
        }
        //把我同步给其他player
        MapMessage.ResMapPlayer.Builder mapPlayer = MapMessage.ResMapPlayer.newBuilder();
        mapPlayer.setPlayer(MapUtils.getPlayerInfo(player));
        MessageUtils.send_to_players(others, MapMessage.ResMapPlayer.MsgID.eMsgID_VALUE, mapPlayer.build().toByteArray(), map.getId());

        if (map.getSetting().getHide_mode() == Hide_Model_1) {
            return;
        }
        //我是否召唤了宠物
        Pet mPet = Manager.petManager.getBattlePet(player);
        if (mPet != null) {
            MapMessage.ResPetBirth.Builder petInfo = MapMessage.ResPetBirth.newBuilder();
            petInfo.setPet(MapUtils.getPetInfo(mPet));
            MessageUtils.send_to_players(others, MapMessage.ResPetBirth.MsgID.eMsgID_VALUE, petInfo.build().toByteArray(), map.getId());
        }
    }

    //进入区域
    private void OnEnterAreas(Player player, List<Area> areas, boolean isfilter, MapMessage.ResRoundObjs.Builder msg) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            return;
        }

        List<Player> others = new ArrayList<>();

        for (Area area : areas) {
            //周围玩家
            for (Player other : area.getPlayers()) {
                if (other.getId() == player.getId()) {
                    continue;
                }
                boolean needb = isfilter ? other.getUnfilters().contains(player.getId()) : true;
                if (!needb) {
                    continue;
                }
                others.add(other);

                msg.addPlayers(MapUtils.getPlayerInfo(other));
                if (map.getSetting().getHide_mode() == Hide_Model_1) {
                    continue;
                }
                Pet pet = Manager.petManager.getBattlePet(other);
                if (pet == null) {
                    continue;
                }
                msg.addPets(MapUtils.getPetInfo(pet));
            }

            for (Robot robot : area.getRobots().values()) {
                if (robot.isDie()) {
                    continue;
                }
                msg.addPlayers(MapUtils.getPlayerInfo(robot));
            }

            for (Monster monster : area.getMonsters().values()) {
                if (monster.isDie()) {
                    continue;
                }
                if (!monster.canSee(player)) {
                    continue;
                }
                msg.addMonsters(MapUtils.getMonsterInfo(monster));
            }
            for (Npc npc : area.getNpcs().values()) {
                if (!npc.canSee(player)) {
                    continue;
                }
                msg.addNpcs(MapUtils.getNpcInfo(npc));
            }
            for (Gather gather : area.getCollects().values()) {
                if (!gather.canSee(player)) {
                    continue;
                }
                msg.addGathers(MapUtils.getGatherInfo(gather));
            }

            for (SkillMagic magic : area.getMagics()) {
                if (!magic.canSee(player)) {
                    continue;
                }
                msg.addMagics(MapUtils.getMagicInfo(magic));
            }

            for (Tombstone tombstone : area.getTombstone().values()) {
                msg.addTombstone(MapUtils.getTombstoneInfo(tombstone));
            }

            for (GroundBuff tombstone : area.getGroundBuffs().values()) {
                msg.addGroundBuff(MapUtils.getGroundBuffInfo(tombstone));
            }
        }

        if (others.isEmpty()) {
            return;
        }
        //把我同步给其他player
        MapMessage.ResMapPlayer.Builder mapPlayer = MapMessage.ResMapPlayer.newBuilder();
        mapPlayer.setPlayer(MapUtils.getPlayerInfo(player));
        MessageUtils.send_to_players(others, MapMessage.ResMapPlayer.MsgID.eMsgID_VALUE, mapPlayer.build().toByteArray(), map.getId());

        if (map.getSetting().getHide_mode() == Hide_Model_1) {
            return;
        }
        //我是否召唤了宠物
        Pet mPet = Manager.petManager.getBattlePet(player);
        if (mPet != null) {
            MapMessage.ResPetBirth.Builder petInfo = MapMessage.ResPetBirth.newBuilder();
            petInfo.setPet(MapUtils.getPetInfo(mPet));
            MessageUtils.send_to_players(others, MapMessage.ResPetBirth.MsgID.eMsgID_VALUE, petInfo.build().toByteArray(), map.getId());
        }

    }

    //退出区域
    private void OnQuitAreas(Player player, List<Area> areas, boolean isfilter, MapMessage.ResRoundObjs.Builder msg) {
        List<Player> others = new ArrayList<>();
        for (Area area : areas) {
            for (Player other : area.getPlayers()) {
                if (other.getId() == player.getId()) {
                    continue;
                }
                others.add(other);
                msg.addRemoveIds(other.getId());
                Pet pet = Manager.petManager.getBattlePet(other);
                if (pet == null) {
                    continue;
                }
                msg.addRemoveIds(pet.getId());

                HuaxinEntity entity = other.getCurHuaxinEntity();
                if (entity == null) {
                    continue;
                }
                msg.addRemoveIds(entity.getId());
            }

            for (Robot robot : area.getRobots().values()) {
                msg.addRemoveIds(robot.getId());
            }
            for (Monster monster : area.getMonsters().values()) {
                msg.addRemoveIds(monster.getId());
            }
            for (Npc npc : area.getNpcs().values()) {
                msg.addRemoveIds(npc.getId());
            }
            for (Gather gather : area.getCollects().values()) {
                msg.addRemoveIds(gather.getId());
            }
            for (SkillMagic magic : area.getMagics()) {
                msg.addRemoveIds(magic.getId());
            }
            for (Tombstone tombstone : area.getTombstone().values()) {
                msg.addRemoveIds(tombstone.getId());
            }

            for (GroundBuff gb : area.getGroundBuffs().values()) {
                msg.addRemoveIds(gb.getId());
            }
        }
        MapMessage.ResPlayerDisappear.Builder disappear = MapMessage.ResPlayerDisappear.newBuilder();
        disappear.addPlayerIds(player.getId());
        Pet selfPet = Manager.petManager.getBattlePet(player);
        if (selfPet != null) {
            disappear.addPlayerIds(selfPet.getId());
        }
        HuaxinEntity huaxinEntity = player.getCurHuaxinEntity();
        if (huaxinEntity != null) {
            disappear.addPlayerIds(huaxinEntity.getId());
        }
        byte[] selfDis = disappear.build().toByteArray();
        MessageUtils.send_to_players(others, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, selfDis, player.getId());
    }

    @Override
    public void OnChangePos(Monster monster, Position oldPos) {
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (null == map) {
            return;
        }
        //影藏怪物
        MapMessage.ResMonsterDisappear.Builder hidemsg = MapMessage.ResMonsterDisappear.newBuilder();
        hidemsg.setMonsterId(monster.getId());

        List<Area> oldAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(map, oldPos));
        List<Area> newAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(monster));

        Set<Player> others = new HashSet<>();
        //公共区域
        List<Area> publicAreas = new ArrayList<>();
        for (Area area : oldAreas) {

            if (newAreas.contains(area)) {
                publicAreas.add(area);
                continue;
            }
            others.addAll(area.getPlayers());
        }
        MessageUtils.send_to_players(others, MapMessage.ResMonsterDisappear.MsgID.eMsgID_VALUE, hidemsg.build().toByteArray(), map.getId());

        others.clear();
        //组装怪物info
        MapMessage.ResMapMonster.Builder showmsg = MapMessage.ResMapMonster.newBuilder();
        showmsg.setMonserInfo(MapUtils.getMonsterInfo(monster));
        //开始遍历当前位置周边区域
        for (Area area : newAreas) {
            if (publicAreas.contains(area)) {
                continue;
            }
            others.addAll(area.getPlayers());
        }
        MessageUtils.send_to_players(others, MapMessage.ResMapMonster.MsgID.eMsgID_VALUE, showmsg.build().toByteArray(), map.getId());
    }

    @Override
    public void OnChangePos(Robot robot, Position oldPos) {
        MapObject map = Manager.mapManager.getMap(robot.gainMapId());
        if (null == map) {
            return;
        }
        Set<Player> others = new HashSet<>();
        //影藏怪物
        MapMessage.ResPlayerDisappear.Builder disappear = MapMessage.ResPlayerDisappear.newBuilder();
        disappear.addPlayerIds(robot.getId());
        if (robot.pet != null) {
            disappear.addPlayerIds(robot.pet.getId());
        }

        List<Area> oldAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(map, oldPos));
        List<Area> newAreas = Manager.mapManager.getRoundAreas(map, Manager.mapManager.getAreaId(robot));

        //公共区域
        List<Area> publicAreas = new ArrayList<>();
        for (Area area : oldAreas) {
            if (newAreas.contains(area)) {
                publicAreas.add(area);
                continue;
            }
            others.addAll(area.getPlayers());
        }
        MessageUtils.send_to_players(others, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, disappear.build().toByteArray(), map.getId());

        others.clear();

        for (Area area : newAreas) {
            if (publicAreas.contains(area)) {
                continue;
            }
            others.addAll(area.getPlayers());
        }
        if (others.isEmpty()) {
            return;
        }
        //把我同步给其他player
        MapMessage.ResMapPlayer.Builder mapPlayer = MapMessage.ResMapPlayer.newBuilder();
        mapPlayer.setPlayer(MapUtils.getPlayerInfo(robot));
        MessageUtils.send_to_players(others, MapMessage.ResMapPlayer.MsgID.eMsgID_VALUE, mapPlayer.build().toByteArray(), map.getId());

        if (map.getSetting().getHide_mode() == Hide_Model_1) {
            return;
        }
        //我是否召唤了宠物
        MapMessage.ResPetBirth.Builder petInfo = MapMessage.ResPetBirth.newBuilder();
        if (robot.pet != null) {
            petInfo.setPet(MapUtils.getPetInfo(robot.pet));
            MessageUtils.send_to_players(others, MapMessage.ResPetBirth.MsgID.eMsgID_VALUE, petInfo.build().toByteArray(), map.getId());
        }
    }

    /**
     * 清理玩家
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public boolean clearPlayer(MapObject map, Player player) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (null != pet) {
            onQuitMap(map, pet, true);
        }
        HuaxinEntity entity = player.getCurHuaxinEntity();
        if (null != entity) {
            onQuitMap(map, entity, true);
        }

        //清理召唤物
        for (long magicId : player.getMagics()) {
            SkillMagic magic = map.getMagic(magicId);
            if (magic == null) {
                continue;
            }
            onQuitMap(map, magic, true);
        }
        player.getMagics().clear();

        // 清除数据
        map.removePlayer(player.getId());
        for (Area area2 : map.getAreas().values()) {
            if (area2.containPlayer(player.getId())) {
                area2.removePlayer(player.getId());
            }
        }
        //通知周围玩家，我离开
        MapMessage.ResPlayerDisappear.Builder msg = MapMessage.ResPlayerDisappear.newBuilder();
        msg.addPlayerIds(player.getId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        //MessageUtils.send_to_roundPlayer(map, player.getCurPos(), ResPlayerDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//        if (GameServer.getInstance().IsFightServer()) {
//            manager.teamManager.deal().Cs_OutTeam(player);
//        }
        //清楚仇恨
        logger.info("玩家(" + player.getId() + ")退出地图(modelId_" + player.gainMapModelId() + " line_" + player.gainLine() + " 地图总人数：" + map.getPlayers().size());
        return true;
    }

    private void quit(MapObject map, Player player, boolean isQuit) {
        if (null == map) {
            return;
        }

        if (player.getRoads() != null) {
            player.clearRoads();
        }
        player.clearHatred();
        player.setCurAttackTargetId(0);

        clearPlayer(map, player);

        if (map.getSetting().getIsscript() > 0) {
            IScript is = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
            if (is instanceof IMapBaseScript) {
                IMapBaseScript cbs = (IMapBaseScript) is;
                cbs.onQuitMap(player, map, isQuit);
                if (map.getZoneModelId() != 0 && isQuit && !map.isStop()) {
                    Manager.copyMapManager.logic().biInstance(player, map.getZoneModelId(), 1, 4, 0, false);
                }
                if (isQuit) {
                    Manager.biManager.getScript().biScene(player, map.getMapModelId(), map.getName(), 4, 0);
                } else {
                    Manager.biManager.getScript().biScene(player, map.getMapModelId(), map.getName(), 2, 0);
                }
            }
        }
    }

    private void quit(MapObject map, Monster monster, boolean isBroadcast) {
        if (null == map) {
            return;
        }
        if (isBroadcast) {
            ResMonsterDisappear.Builder msg = ResMonsterDisappear.newBuilder();
            msg.setMonsterId(monster.getId());
            MessageUtils.send_to_roundPlayer(monster, ResMonsterDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        Area area = Manager.mapManager.getArea(monster.gainCurPos(), map);
        if (null == area) {
            return;
        }
        area.getMonsters().remove(monster.getId());

        map.getMonsters().remove(monster.getId());
    }

    private void quit(MapObject map, Npc npc) {
        if (null == npc) {
            return;
        }
        if (null == map) {
            return;
        }
        map.getNpcs().remove(npc.getId());
        Area area = Manager.mapManager.getArea(npc.gainCurPos(), map);
        if (null == area) {
            return;
        }
        area.getNpcs().remove(npc.getId());

        ResRoundNpcDisappear.Builder msg = ResRoundNpcDisappear.newBuilder();
        msg.addNpcIds(npc.getId());

        //广播周围怪物消失
        MessageUtils.send_to_roundPlayer(npc, ResRoundNpcDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    private void quit(MapObject map, Pet pet) {
        if (null == pet) {
            return;
        }
        if (null == map) {
            return;
        }
        if (pet.getRoads() != null) {
            pet.clearRoads();
        }
        MapMessage.ResPetDisappear.Builder msg = MapMessage.ResPetDisappear.newBuilder();
        msg.setId(pet.getId());
        //广播周围怪物消失
        MessageUtils.send_to_roundPlayer(pet, MapMessage.ResPetDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    private void quit(MapObject map, SkillMagic magic) {
        if (null == magic) {
            return;
        }
        if (null == map) {
            return;
        }
        map.removeMagic(magic.getId());
        Area area = Manager.mapManager.getArea(magic.gainCurPos(), map);
        if (null == area) {
            return;
        }
        area.removeMagic(magic.getId());

        MapMessage.ResMagicClean.Builder msg = MapMessage.ResMagicClean.newBuilder();
        msg.setId(magic.getId());

        //广播周围怪物消失
        MessageUtils.send_to_roundPlayer(magic, ResMagicClean.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);

    }

    private void quit(MapObject map, Gather gather) {
        if (null == gather) {
            return;
        }
        if (null == map) {
            return;
        }
        map.getCollects().remove(gather.getId());
        Area area = Manager.mapManager.getArea(gather.gainCurPos(), map);
        if (null == area) {
            return;
        }
        area.getCollects().remove(gather.getId());

        MapMessage.ResGatherDisappear.Builder msg = MapMessage.ResGatherDisappear.newBuilder();
        msg.setId(gather.getId());

        //广播周围怪物消失
        MessageUtils.send_to_roundPlayer(gather, MapMessage.ResGatherDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    //墓碑退出
    private void quit(MapObject map, Tombstone tombstone) {
        if (null == tombstone) {
            return;
        }

        if (null == map) {
            return;
        }
        map.getTombstone().remove(tombstone.getId());
        Area area = Manager.mapManager.getArea(tombstone.gainCurPos(), map);
        if (area == null) {
            return;
        }
        area.getTombstone().remove(tombstone.getId());

        ResTombstoneClean.Builder msg = ResTombstoneClean.newBuilder();
        msg.setId(tombstone.getId());
        MessageUtils.send_to_roundPlayer(tombstone, MapMessage.ResTombstoneClean.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    //地图BUFF的退出
    private void quit(MapObject map, GroundBuff gb) {
        if (null == gb) {
            return;
        }

        if (null == map) {
            return;
        }
        map.getGroundBuffs().remove(gb.getId());
        Area area = Manager.mapManager.getArea(gb.gainCurPos(), map);
        if (area == null) {
            return;
        }
        area.getGroundBuffs().remove(gb.getId());

        ResGroundBuffClean.Builder msg = ResGroundBuffClean.newBuilder();
        msg.setGbid(gb.getId());
        MessageUtils.send_to_roundPlayer(gb, MapMessage.ResGroundBuffClean.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    //地图机器人退出
    private void quit(MapObject map, Robot robot) {
        if (null == robot) {
            return;
        }

        if (null == map) {
            return;
        }

        map.getRobots().remove(robot.getId());
        Area area = Manager.mapManager.getArea(robot.gainCurPos(), map);
        if (area == null) {
            return;
        }
        area.getRobots().remove(robot.getId());

        MapMessage.ResPlayerDisappear.Builder disappear = MapMessage.ResPlayerDisappear.newBuilder();
        disappear.addPlayerIds(robot.getId());
        if (robot.pet != null) {
            disappear.addPlayerIds(robot.pet.getId());
        }
        MessageUtils.send_to_roundPlayer(robot, MapMessage.ResPlayerDisappear.MsgID.eMsgID_VALUE, disappear.build().toByteArray(), false);


        robot.setState(EntityState.Dead.getValue());
    }

    //机器人进入
    private void enter(Robot robot) {
        logger.info("机器人进入地图" + robot);

        MapObject map = Manager.mapManager.getMap(robot.gainMapId());
        if (null == map) {
            logger.error("进入地图失败" + robot);
            return;
        }

        map.getRobots().put(robot.getId(), robot);

        Area area = Manager.mapManager.getArea(robot.gainCurPos(), map);

        if (null == area) {
            map.getRobots().remove(robot.getId());
            logger.error("进入区域失败" + robot);
            return;
        }
        area.getRobots().put(robot.getId(), robot);

        ResMapPlayer.Builder mRobot = ResMapPlayer.newBuilder();
        mRobot.setPlayer(MapUtils.getPlayerInfo(robot));
        MessageUtils.send_to_roundPlayer(robot, ResMapPlayer.MsgID.eMsgID_VALUE, mRobot.build().toByteArray(), true);
        logger.error("消息发送成功  ResMapPlayer");

    }

    private void enter(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO 打印堆栈 测试用
    private String getStrack() {
        Exception exception = new Exception();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        for (StackTraceElement stackTraceElement : stackTrace) {
            buffer.append(stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName()).append(".").append(stackTraceElement.getLineNumber()).append("\n");
        }
        return buffer.toString();
    }

    private void enter(Monster monster) {
        MapObject map = Manager.mapManager.getMap(monster.gainMapId());
        if (null == map) {
            logger.error("monster" + monster.getId() + "[" + monster.getName() + "]" + "进入地图失败");
            return;
        }

        Cfg_Monster_Bean config = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config.getBOSS_type() == 3 && mapBean.getScene_came_match_type() == 1) {
            monster.setCamp(1, true);
        }
        //死亡复活或者重复进入的
        if (map.getMonsters().containsKey(monster.getId())) {
            //要删除老的
            Area area = Manager.mapManager.getArea(monster.gainCurPos(), map);
            if (area != null) {
                area.getMonsters().remove(monster.getId());
            }
            map.getMonsters().remove(monster.getId());
            monster.setId(IDConfigUtil.getLogId());
        }
        map.getMonsters().put(monster.getId(), monster);
        Area area = Manager.mapManager.getArea(monster.gainCurPos(), map);
        if (null == area) {
            map.getMonsters().remove(monster.getId());
            logger.error("monster" + monster.getId() + "[" + monster.getModelId() + "_" + monster.getName() + "] pos {" + monster.gainX() + "," + monster.gainY() + "}进入区域失败");
            return;
        }

        area.getMonsters().put(monster.getId(), monster);

        Manager.monsterManager.ai().setConfigYedAi(monster, config);

        ResMapMonster.Builder msg = ResMapMonster.newBuilder();
        msg.setMonserInfo(MapUtils.getMonsterInfo(monster));
        List<Area> areas = Manager.mapManager.getRounds(map, monster.gainCurPos());
        List<Player> players = new ArrayList<>();
        for (Area tempArea : areas) {
            players.addAll(tempArea.getPlayers());
        }

        for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
            Player player = it.next();
            if (!Manager.monsterManager.taskIsShow().canSee(player, monster)) {
                it.remove();
            }
        }

        MessageUtils.send_to_players(players, ResMapMonster.MsgID.eMsgID_VALUE, msg.build().toByteArray(), map.getId());
    }

    private void enter(Pet pet) {
        MapObject map = Manager.mapManager.getMap(pet.gainMapId());
        if (null == map) {
            logger.error(pet.getName() + "pet进入地图失败");
            return;
        }
        if (map.getSetting().getHide_mode() == Hide_Model_1) {
            return;
        }
        MapMessage.ResPetBirth.Builder msg = MapMessage.ResPetBirth.newBuilder();
        msg.setPet(MapUtils.getPetInfo(pet));
        MessageUtils.send_to_roundPlayer(pet, MapMessage.ResPetBirth.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    private void enter(Npc npc) {
        MapObject map = Manager.mapManager.getMap(npc.gainMapId());
        if (null == map) {
            logger.error(npc.getName() + "进入地图失败npc");
            return;
        }
        map.getNpcs().put(npc.getId(), npc);
        Area area = Manager.mapManager.getArea(npc.gainCurPos(), map);
        if (null == area) {
            map.getNpcs().remove(npc.getId());
            logger.error(npc.getName() + "进入区域失败错误坐标npc");
            return;
        }
        area.getNpcs().put(npc.getId(), npc);
        //LOGGER.error( "map:" + map.getMapModelId() + " own npc size:" + map.getNpcs().size());
        ResRoundNpcInfo.Builder msg = ResRoundNpcInfo.newBuilder();
        msg.setNpcInfo(MapUtils.getNpcInfo(npc));
        //广播周围玩家，npc进入地图
        MessageUtils.send_to_roundPlayer(npc, ResRoundNpcInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        //MessageUtils.send_to_roundPlayer(map, npc.getCurPos(), ResRoundNpcInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void enter(Gather gather) {
        MapObject map = Manager.mapManager.getMap(gather.gainMapId());
        if (null == map) {
            logger.error(gather + "进入地图失败");
            return;
        }
        map.getCollects().put(gather.getId(), gather);
        Area area = Manager.mapManager.getArea(gather.gainCurPos(), map);
        if (null == area) {
            map.getCollects().remove(gather.getId());
            logger.error(gather + "进入区域失败错误坐标 采集物");
            return;
        }
        area.getCollects().put(gather.getId(), gather);
        // LOGGER.error( "map:" + map.getMapModelId() + " own gather size:" + map.getCollects().size());
        MapMessage.ResMapGatherInfo.Builder msg = MapMessage.ResMapGatherInfo.newBuilder();
        msg.setGatherInfo(MapUtils.getGatherInfo(gather));
        //广播周围玩家，采集物进入地图
        MessageUtils.send_to_roundPlayer(gather, MapMessage.ResMapGatherInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        //MessageUtils.send_to_roundPlayer(map, gather.getCurPos(), ResMapGatherInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void enter(SkillMagic magic) {
        MapObject map = Manager.mapManager.getMap(magic.gainMapId());
        if (null == map) {
            logger.error(magic.getModelId() + "进入地图失败");
            return;
        }
        map.addMagic(magic);
        Area area = Manager.mapManager.getArea(magic.gainCurPos(), map);
        if (null == area) {
            map.removeMagic(magic.getId());
            logger.error(magic.getModelId() + "进入区域失败");
            return;
        }
        area.addMagic(magic);
        MapMessage.ResMagicBirth.Builder msg = MapMessage.ResMagicBirth.newBuilder();
        msg.setMagic(MapUtils.getMagicInfo(magic));
        MessageUtils.send_to_roundPlayer(magic, ResMagicBirth.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    private void enter(GroundBuff gb) {
        MapObject map = Manager.mapManager.getMap(gb.gainMapId());
        if (null == map) {
            logger.error(gb.getModelId() + "进入地图失败");
            return;
        }
        map.getGroundBuffs().put(gb.getId(), gb);
        Area area = Manager.mapManager.getArea(gb.gainCurPos(), map);
        if (null == area) {
            map.removeMagic(gb.getId());
            logger.error(gb.getModelId() + "进入区域失败");
            return;
        }
        area.getGroundBuffs().put(gb.getId(), gb);
        ResGroundBuffBirth.Builder msg = ResGroundBuffBirth.newBuilder();
        msg.setGroundBuffInfo(MapUtils.getGroundBuffInfo(gb));
        MessageUtils.send_to_roundPlayer(gb, ResGroundBuffBirth.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    private void enter(Tombstone tombstone) {
        MapObject map = Manager.mapManager.getMap(tombstone.gainMapId());
        if (null == map) {
            logger.error(tombstone + "进入地图失败");
            return;
        }
        map.getTombstone().put(tombstone.getId(), tombstone);
        Area area = Manager.mapManager.getArea(tombstone.gainCurPos(), map);
        if (null == area) {
            map.getTombstone().remove(tombstone.getId());
            logger.error(tombstone + "进入区域失败错误坐标 采集物");
            return;
        }
        area.getTombstone().put(tombstone.getId(), tombstone);
        MapMessage.ResTombstoneBirth.Builder msg = MapMessage.ResTombstoneBirth.newBuilder();
        msg.setTombstoneInfo(MapUtils.getTombstoneInfo(tombstone));
        MessageUtils.send_to_roundPlayer(tombstone, MapMessage.ResTombstoneBirth.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    @Override
    public void deleteMap(MapObject mapObject) {
        MapServerGroup mapServerGroup;
        MapServer mapServer;

        if (mapObject.getType() == MapDefine.WORLD_MAP) {
            mapServerGroup = GameServer.getInstance().getWorldMapServerGroup();
        } else {
            mapServerGroup = GameServer.getInstance().getCopyMapServerGroup();
        }
        if (mapServerGroup == null) {
            logger.error("移除地图出错,线程组不存在了,无法移除地图[mapId=" + mapObject.getId() + ", mapModelId="
                    + mapObject.getMapModelId() + ", line=" + mapObject.getLineId() + "]");
            return;
        }
        mapServer = mapServerGroup.removeMap(mapObject.getId());

        if (mapServer == null) {
            logger.error("移除地图出错,无法移除地图[mapId=" + mapObject.getId() + ", mapModelId="
                    + mapObject.getMapModelId() + ", line=" + mapObject.getLineId() + "]");
            return;
        }

        mapServer.setNum(mapServer.getNum() - 1);
        mapServerGroup.removeMapServer(mapServer);
        logger.info("移除地图成功,移除地图[mapId=" + mapObject.getId() + ", mapModelId="
                + mapObject.getMapModelId() + ", line=" + mapObject.getLineId() + "]在地图服务器" + mapObject.getName());
    }

    @Override
    public void createMap(MapObject mapObject, Object[] objects) {
        MapServer mapServer;
        if (mapObject.getType() == MapDefine.WORLD_MAP) {
            mapServer = GameServer.getInstance().getWorldMapServerGroup().addMapServer(mapObject.getId());
        } else {
            mapServer = GameServer.getInstance().getCopyMapServerGroup().addMapServer(mapObject.getId());
        }

        if (mapServer == null) {
            logger.error("创建地图出错,无法创建地图[serverKey=" + ", mapId=" + mapObject.getId() + ", mapModelId="
                    + mapObject.getMapModelId() + ", line=" + mapObject.getLineId() + "]");
            return;
        }

        mapServer.setNum(mapServer.getNum() + 1);
        logger.info("创建地图成功,创建地图[mapId=" + mapObject.getId() + ", mapModelId="
                + mapObject.getMapModelId() + ", line=" + mapObject.getLineId() + "]在地图服务器" + mapServer.getName());
        mapObject.addTimerEvent(new MonsterBehaviorTimer(mapObject));
        mapObject.addTimerEvent(new PlayerBehaviorTimer(mapObject));
        mapObject.addTimerEvent(new MapHeartTimer(mapObject));
        mapObject.addTimerEvent(new PlayerHeartTimer(mapObject));
        mapObject.addTimerEvent(new RobotBehaviorTimer(mapObject));
        if (!GameServer.getInstance().IsFightServer()) {
            mapObject.addTimerEvent(new MapSavePlayerTimer(mapObject));
        }

        if (mapObject.getSetting().getIsscript() > 0) {
            Manager.mapManager.base(mapObject.getSetting().getIsscript()).onCreate(mapObject, objects);
        }
    }

    @Override
    public void changeMap(Player player, long mapId, int modelId, int line, Position pos, int type, boolean isLogin) {

        logger.info("角色开始切换地图player={} mapId={} modelId={} line={} pos={} type={} login={}", player, mapId, modelId, line, pos, type, isLogin);

        if (player.playerCrossData.isToFightServer()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CROSSWAR_DOING);
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(mapId);
        if (mapObject != null && mapObject.isStop()) {
            logger.error("地图已经停止,不能进入" + mapId);
            mapObject = null;
        }

        if (modelId == 0) {
//            logger.error("找不到地图，回到新手村地图");
            modelId = Manager.playerManager.getBornMapID();
        }
        if (modelId == MapManager.SpecBossMapId && !Manager.mapManager.base(ScriptEnum.WorldBossActivityScript).canEnterMap(player, MapManager.SpecBossMapId, 0)) {
            modelId = Manager.playerManager.getBornMapID();
        }
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(modelId);

        if (mapObject == null) {
            if (bean.getType() != MapDefine.WORLD_MAP) {
                logger.error("获取地图线路的地图类型错误,回到新手村" + Manager.playerManager.getBornMapID());
                modelId = Manager.playerManager.getBornMapID();
                bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(modelId);
            }
            if (line == 0) {
                //通过组队获取队友的线
                line = Manager.teamManager.deal().gainTeamMapLine(player, modelId);
            }

            if (line != 0) {
                mapObject = getLineWorldMap(bean, line);
            }

            if (mapObject == null) {
                mapObject = getWorldMap(bean);
            }

            if (mapObject == null) {
                logger.error("进入野图失败,无法分配线路" + modelId);
                return;
            }
        } else {
            bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapObject.getMapModelId());
        }

        if (bean.getType() == MapDefine.WORLD_MAP) {
            player.setBrithProtect(TimeUtils.Time() + 8000);
            //直接进去
        } else {
            //人数已满
            if (mapObject.getPlayers().size() >= bean.getOnline()) {
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.CopyMap_Full_PLAYER);
                logger.error("进入副本地图人数已满");
                return;
            }
        }

        if (pos == null) {
            pos = mapObject.getBrithPos();
        } else {
            if (!Utils.isCanMove(mapObject, pos)) {
                pos = mapObject.getBrithPos();
            }
        }

        MapObject oldMapObject = Manager.mapManager.getMap(player.gainMapId());

        //如果是同地图
        if (oldMapObject != null && !isLogin) {
            if (mapObject.equals(oldMapObject)) {
                player.changeCurPos(pos, true);
                MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
                msg.setId(player.getId());
                msg.setTarget(MapUtils.getPos(pos));
                MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                return;
            }

            onQuitMap(oldMapObject, player, true);

            if (oldMapObject.getType() == MapDefine.WORLD_MAP && oldMapObject.getMapModelId() != MapManager.CrossWaitMapId) {
                MapGpsUtil.CopyGPS(player.getCurGps(), player.getOld());
            }
        }

        BehaviorManager.CancelAllBehavior(player);

        MapGpsUtil.CopyGPS(mapObject.getGps(pos), player.getCurGps());

        //检查组队BUFF
        if (player.getTeamId() > 0) {
            Manager.teamManager.deal().checkTeamBuff(player);
        }

        logger.info("角色等待客户端切换地图player:" + player.toString() + ";toMapModelid:" + mapObject.getMapModelId() + ";mapid:" + mapObject.getId() + ";line:" + mapObject.getLineId() + ";pos:" + pos.toString());

        if (isLogin) {
            MapUtils.sendPlayerLoadingMapID(player);
            Manager.biManager.getScript().biScene(player, mapObject.getMapModelId(), mapObject.getName(), 1, 0);
        } else {
            player.addState(EntityState.ChangeMap);
            Manager.biManager.getScript().biScene(player, mapObject.getMapModelId(), mapObject.getName(), 3, 0);
            MapUtils.sendChangeMap(player, mapObject.getMapModelId(), mapObject.getLineId(), pos, MapDefine.CHANGE_MAP_RESULT_SUCCESS, type, -1);
        }
    }

    @Override
    public void changeSpaceMap(Player player) {
        MapObject oldmap = Manager.mapManager.getMap(player.gainMapId());
        if (oldmap.getType() != MapDefine.WORLD_MAP) {
            logger.error(oldmap.getName() + " ,type=" + oldmap.getType() + "不是允许设置的老地图！");
        }

        MapGps gps = player.getOld();
        gps.setLine(oldmap.getLineId());
        gps.setMapId(oldmap.getId());
        gps.setModelId(oldmap.getMapModelId());

        //处理进入跨服时候，老坐标为空时候，从新new一个，避免退出跨服，退不出来
        if (gps.getPos() == null) {
            Position pos = new Position(player.gainCurPos().getX(), player.gainCurPos().getY());//坐标
            gps.setPos(pos);
        } else {
            gps.getPos().setX(player.gainCurPos().getX());
            gps.getPos().setY(player.gainCurPos().getY());
        }
        player.setOld(gps);
        Manager.mapManager.manager().onQuitMap(oldmap, player, true);

        //获取跨服休息室
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(MapManager.CrossWaitMapId);
        MapObject spaceMap = getWorldMap(bean);
        player.changeMapId(spaceMap.getId());
        player.changeMapModelId(spaceMap.getMapModelId());
        player.changeLine(spaceMap.getLineId());
        spaceMap.addPlayer(player.getId(), player);
        logger.info("玩家真正  进入跨服休息室  " + player.gainMapModelId());
    }

    private MapObject initMap(int mapModelID, long mapId, int lineId, Object... objects) {
        ByteMapCfg mapCfg = MapsConfigManager.getInstance().getMapCfg(mapModelID);

        Cfg_Mapsetting_Bean mapSet = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapModelID);

        MapObject mapObject = new MapObject();

        if (mapSet.getFilter() > 0) {
            int num = mapSet.getFilter_num();
            if (mapSet.getFilter_num() < 1) {
                num = 1;
            }
            mapObject.setFilterNum(num);
        }
        mapObject.setSetting(mapSet);
        mapObject.setLineId(lineId);
        mapObject.setId(mapId);
        mapObject.setMapModelId(mapSet.getMap_id());
        mapObject.setType(mapSet.getType());
        mapObject.setName(mapSet.getName());
        mapObject.setColCellCount(mapCfg.getColCellCount());
        mapObject.setRowCellCount(mapCfg.getRowCellCount());
        mapObject.setCreate(TimeUtils.Time());
        mapObject.setPkState(mapSet.getPkState());
        mapObject.setArea_high(mapSet.getArea_high());
        mapObject.setArea_width(mapSet.getArea_width());
        Manager.mapManager.getMaps().put(mapObject.getId(), mapObject);

        initArea(mapObject);
        initRelivePoint(mapObject);
        initNpc(mapObject, mapCfg);
        initMonster(mapObject, mapCfg);
        initGather(mapObject, mapCfg);
        initTransport(mapObject, mapCfg);
        initDynamicBlock(mapObject, mapCfg);
        initGroundBuff(mapObject, mapCfg);
        return mapObject;
    }

    private void initDynamicBlock(MapObject map, ByteMapCfg mapCfg) {
        for (ByteDynamicBlock config : mapCfg.getDynamics()) {
            DynamicBlock block = new DynamicBlock();
            block.init(map, config);
            map.getDoors().put(block.getKey(), block);
            logger.info("动态阻挡生成中心点=" + block.getCenter() + "size=" + block.getBlocks().size());
        }
    }

    private void initRelivePoint(MapObject map) {
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config == null) {
            return;
        }

        ReadIntegerArrayEs splits = config.getBornPosition();
        if (splits == null || splits.isEmpty() || splits.size() == 0) {
            return;
        }

        for (ReadArray<Integer> split : splits.getValuees()) {
            if (split == null) {
                continue;
            }
            if (split.size() < 2) {
                continue;
            }
            float x = split.get(0);
            float f = split.get(1);
            Position point = new Position();
            point.setX(x);
            point.setY(f);
            map.getBriths().add(point);
        }

        //开始解析复活点
        ReadIntegerArrayEs splits2 = config.getRelivePosition();
        if (splits2 == null || splits2.size() == 0) {
            return;
        }

        for (ReadArray<Integer> split : splits2.getValuees()) {
            if (split == null) {
                continue;
            }
            if (split.size() < 2) {
                continue;
            }
            float x = split.get(0) * 1.0F;
            float y = split.get(1) * 1.0F;
            Position point = new Position();
            point.setX(x);
            point.setY(y);
            map.getRelives().add(point);
        }
    }

    private void initTransport(MapObject map, ByteMapCfg mapCfg) {
        Iterator<ByteMapTrigger> iter = mapCfg.getTriggerCfg().iterator();
        while (iter.hasNext()) {
            ByteMapTrigger tcfg = iter.next();
            Transport port = new Transport();
            port.setInitPos(tcfg.getPos());
            port.setId(tcfg.getModelID());
            if (TriggerType.TransPost.compare(tcfg.getType())) {
                int TargetMapId = Integer.parseInt(tcfg.getInArgs().get(0));
                port.setTargetID(TargetMapId);
                float x = Float.valueOf(tcfg.getInArgs().get(1));
                float y = Float.valueOf(tcfg.getInArgs().get(2));
                port.setTargetPos(MapManager.getPos(x, y));
            }
            if (TriggerType.FlyTransPost.compare(tcfg.getType())) {
                int TargetMapId = Integer.parseInt(tcfg.getInArgs().get(0));
                port.setTargetID(TargetMapId);
                port.setJump(true);
            }
            map.getTransports().put(port.getId(), port);
        }
    }

    private void initArea(MapObject map) {
        int areaCol = (map.getColCellCount() % map.getArea_width() == 0) ? (map.getColCellCount() / map.getArea_width()) : (map.getColCellCount() / map.getArea_width() + 1);
        int areaRow = (map.getRowCellCount() % map.getArea_high() == 0) ? (map.getRowCellCount() / map.getArea_high()) : (map.getRowCellCount() / map.getArea_high() + 1);
        map.setAreaCol(areaCol);
        map.setAreaRow(areaRow);
        Area area;
        for (int i = 0; i < areaCol; i++) {
            for (int j = 0; j < areaRow; j++) {
                area = new Area();
                area.setId(i * Manager.mapManager.AreaArg + j);
                map.getAreas().put((int) area.getId(), area);
            }
        }
    }

    private void initMonster(MapObject map, ByteMapCfg mapCfg) {
        Iterator<ByteMapItem> iter = mapCfg.getMonsterCfg().iterator();
        ByteMapItem item;
        while (iter.hasNext()) {
            item = iter.next();
            Manager.monsterManager.createMonster(map, item.getPos(), item.getId());
        }
    }

    private void initGather(MapObject map, ByteMapCfg mapCfg) {
        Iterator<ByteMapItem> iter = mapCfg.getCollectCfg().iterator();
        while (iter.hasNext()) {
            ByteMapItem item = iter.next();
            Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(item.getId());
            if (null == gatherCfg) {
                logger.error("初始化采集物" + item.getId() + "失败， 没有这个配置");
                continue;
            }
            Manager.gatherManager.deal().createGather(map, gatherCfg, item.getPos());
        }
    }

    private void initNpc(MapObject map, ByteMapCfg mapCfg) {
        List<ByteMapItem> npclist = mapCfg.getNpcCfg();
        for (ByteMapItem npcParams : npclist) {
            Cfg_Npc_Bean bean = CfgManager.getCfg_Npc_Container().getValueByKey(npcParams.getId());
            if (bean == null) {
                logger.error("找不到NPCID：" + npcParams.getId());
                continue;
            }
            Npc npc = new Npc();
            npc.setId(bean.getId());
            npc.changeMapModelId(map.getMapModelId());
            npc.setModelId(npcParams.getId());
            npc.changeMapId(map.getId());
            npc.changeLine(map.getLineId());
            npc.changeCurPos(npcParams.getPos());
            npc.setNpcDir((int) npcParams.getRotW());
            Manager.mapManager.manager().onEnterMap(npc);
        }
    }

    private void initGroundBuff(MapObject map, ByteMapCfg mapCfg) {
        Iterator<ByteMapItem> iter = mapCfg.getGroundBuffCfg().iterator();
        while (iter.hasNext()) {
            ByteMapItem item = iter.next();
            Cfg_GroundBuff_Bean gatherCfg = CfgManager.getCfg_GroundBuff_Container().getValueByKey(item.getId());
            if (null == gatherCfg) {
                logger.error("初始化地图BUFF" + item.getId() + "失败， 没有这个配置");
                continue;
            }
            GroundBuff ground = new GroundBuff();
            ground.setModelId(gatherCfg.getId());
            ground.setPos(item.getPos());
            ground.setMapId(map.getId());
            ground.setLine(map.getLineId());
            ground.setCreateTime(TimeUtils.Time());
            ground.setLogicSize(gatherCfg.getLogic_body_size());
            ground.setType(0);
            ground.setGroupNo(gatherCfg.getGroupNo());
            Manager.mapManager.manager().onEnterMap(ground);
        }
    }

    private MapObject getLineWorldMap(Cfg_Mapsetting_Bean bean, int line) {
        ArrayList<MapObject> allLines = Manager.mapManager.getMaps(bean.getMap_id());
        if (null == allLines) {
            logger.error("地图当前无线路 mapModelID=" + bean.getMap_id() + "line:" + line);
            return null;
        }

        for (MapObject mapObject : allLines) {
            if (mapObject.getLineId() == line) {
                if (mapObject.getPlayers().size() >= bean.getOnline()) {
                    logger.error("地图当前人数已满 mapModelID=" + bean.getMap_id() + "line:" + line);
                    return null;
                } else {
                    return mapObject;
                }
            }
        }
        logger.error("未找到当前线路 mapModelID=" + bean.getMap_id() + "line:" + line);
        return null;
    }

    private MapObject getWorldMap(Cfg_Mapsetting_Bean bean) {
        if (bean.getType() != MapDefine.WORLD_MAP) {
            logger.error("获取地图线路的地图类型错误:" + bean.getType());
            return null;
        }

        ArrayList<MapObject> allLines = Manager.mapManager.getMaps(bean.getMap_id());
        if (null == allLines) {
            MapObject map = createWorldMap(bean, 1);
            if (map == null) {
                logger.error("首次创建世界地图失败 mapModelID=" + bean.getMap_id());
            }
            return map;
        }

        //有空闲线路
        for (MapObject mapObject : allLines) {
            if (mapObject.getPlayers().size() >= bean.getOnline()) {
                continue;
            }
            return mapObject;
        }

        if (bean.getLines() > allLines.size()) {
            //创建新线路
            int maxLine = 0;
            for (MapObject mapObject : allLines) {
                if (mapObject.getLineId() > maxLine) {
                    maxLine = mapObject.getLineId();
                }
            }

            if (maxLine == 0) {
                logger.error("创建世界地图失败,找不到线路 mapModelID=" + bean.getMap_id());
                return null;
            }
            MapObject map = createWorldMap(bean, maxLine + 1);
            if (map == null) {
                logger.error("创建世界地图失败, mapModelID=" + bean.getMap_id());
            }
            return map;
        } else {
            //线路是否已全部满了
            logger.error("获取地图线路已经创建满了,只有再次平均分配 mapModelID=" + bean.getMap_id());
            MapObject minMap = null;
            for (MapObject mapObject : allLines) {
                if (null == minMap) {
                    minMap = mapObject;
                    continue;
                }
                if (mapObject.getPlayers().size() < minMap.getPlayers().size()) {
                    minMap = mapObject;
                }
            }
            return minMap;
        }
    }

}
