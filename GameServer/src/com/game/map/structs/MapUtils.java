package com.game.map.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_Relive_Bean;
import com.data.struct.ReadIntegerArray;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.buff.structs.Buff;
import com.game.fightserver.manager.FightClientManager;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.marriage.struct.MarryChild;
import com.game.monster.structs.Monster;
import com.game.nature.structs.HuaxinEntity;
import com.game.newfashion.manager.NewFashionManager;
import com.game.npc.structs.Npc;
import com.game.npc.structs.Tombstone;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.robot.struct.Robot;
import com.game.server.GameServer;
import com.game.skill.structs.SkillMagic;
import com.game.statestifle.structs.SoulSpiritInfo;
import com.game.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.util.StringUtils;
import game.core.util.TimeUtils;
import game.message.*;
import game.message.EquipMessage.ResEquipMinStar;
import game.message.FightMessage.ResUpdateFightState;
import game.message.MapMessage.ResMoveSpeedChange;
import game.message.MapMessage.ResUpdateCamp;
import game.message.PlayerMessage.ResFightOrUnFight;

import java.util.*;


public class MapUtils {
    // 0 全部 -1 全不要 第0位1不要怪 第1位不要角色 第2位不要宠物(暂时还没有) 第3位不要机器人
    public enum FighterFlag {

        NoMonster(1),
        NoPlayer(1 << 1),
        NoRobot(1 << 2),
        Player(~NoPlayer.v),
        Monster(~NoMonster.v),
        Robot(~NoRobot.v),
        All(0),
        ;

        FighterFlag(int v) {
            this.v = v;
        }

        public int v = 0;
    }

    //获取地图玩家信息
    public static CommonMessage.PlayerInfo.Builder getPlayerInfo(Robot robot) {
        CommonMessage.PlayerInfo.Builder playerInfo = CommonMessage.PlayerInfo.newBuilder();
        playerInfo.setPlayerId(robot.getId());
        playerInfo.setName(robot.getName());
        playerInfo.setCareer(robot.getCareer());
        playerInfo.setLevel(robot.getLevel());
        playerInfo.setX(robot.gainX());
        playerInfo.setY(robot.gainY());
        playerInfo.setCurHp(robot.getCurHp());
        playerInfo.setMaxHp(robot.getAttribute().MaxHP());
        playerInfo.setMoveSpeedFinal(robot.getAttribute().gainFinalMoveSpeed());
        playerInfo.setAttackSpeedFinal((int) robot.getAttribute().getAdditionValue(AttributeType.AttackSpeedFinal));
        playerInfo.setFightState(robot.getFightState());
        playerInfo.setMountId(robot.getMountId());
        playerInfo.setChildId(0);
        playerInfo.setFightPower((int) robot.getFightPoint());

        if (robot.getRoads() != null) {
            for (Position pos : robot.getRoads()) {
                playerInfo.addPosList(MapUtils.getPos(pos));
            }
        }
        if (robot.getDir() == null) {
            playerInfo.setDirX(1);
            playerInfo.setDirY(0);
        } else {
            playerInfo.setDirX(robot.getDir().getX());
            playerInfo.setDirY(robot.getDir().getY());
        }
        playerInfo.setGuildId(robot.getGuildId());
        playerInfo.setGuildName(robot.getGuildName());
        playerInfo.setGuildPost(robot.getGuildPost());

        playerInfo.setTitle(robot.getTitle());

        playerInfo.setIsGather(EntityState.Pick.compare(robot.getState()));
        playerInfo.setStateVip(5);
        playerInfo.setOnSitting(false);
        playerInfo.setCamp(robot.getCamp());
        playerInfo.setShiHaiCfgId(0);
        playerInfo.setMarriageOtherName("");
        playerInfo.setCurWakan(robot.getCurWakan());
        playerInfo.setMaxWakan(robot.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan));
        for (Buff buff : robot.getBuffs()) {
            CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
            playerInfo.addBuffList(builder);
        }
        playerInfo.setSoulSpirte1(0);
        playerInfo.setSoulSpirte2(0);
        playerInfo.setSoulSpirte3(0);
        playerInfo.setFeijianId(0);
        playerInfo.setFeijianUid(0);
        playerInfo.setFacade(getFacade(robot.getWingId(), robot.getFashionBodyId(), robot.getFashionHalo(), robot.getFashionMatrix(), robot.getFashionWeaponId(), robot.getSpiritId(), robot.getSoulArmorId()));



        return playerInfo;
    }

    //获取地图玩家信息
    public static CommonMessage.PlayerInfo.Builder getPlayerInfo(Player player) {
        CommonMessage.PlayerInfo.Builder playerInfo = CommonMessage.PlayerInfo.newBuilder();
        playerInfo.setPlayerId(player.getId());
        playerInfo.setName(player.getName());
        playerInfo.setCareer(player.getCareer());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setX(player.gainX());
        playerInfo.setY(player.gainY());
        playerInfo.setStateVip(player.getStateVip().getLv());
        playerInfo.setCurHp(player.getCurHp());
        playerInfo.setMaxHp(player.getAttribute().MaxHP());
        playerInfo.setMoveSpeedFinal(player.getAttribute().gainFinalMoveSpeed());
        playerInfo.setAttackSpeedFinal((int) player.getAttribute().getAdditionValue(AttributeType.AttackSpeedFinal));//player.getAttribute().gainFinalAttackSpeed());
        playerInfo.setFightState(player.getFightState());
        playerInfo.setMountId(player.getHorse().getRideState() == HorseRideStateEnum.UnRide ? 0 : player.getHorse().getHorseModelId());
        playerInfo.setFabaoId(player.getStifleData().getNature().getCurrentModelId());
        playerInfo.setFabaoUid(player.getStifleData().getNature().getId());
        if (player.getHorse().isRideOther()) {
            playerInfo.setMountId(0);
        }
        playerInfo.setChildId(player.getChildId());
        playerInfo.setFightPower((int) player.getFightPoint());
        if (player.getRoads() != null) {
            try {
                List<Position> poslist = new ArrayList<>(player.getRoads());
                for (Position pos : poslist) {
                    playerInfo.addPosList(MapUtils.getPos(pos));
                }
            } catch (Exception e) {
//                LOGGER.error(e, e);
            }
        }
        if (player.getDir() == null) {
            playerInfo.setDirX(1);
            playerInfo.setDirY(0);
        } else {
            playerInfo.setDirX(player.getDir().getX());
            playerInfo.setDirY(player.getDir().getY());
        }
        playerInfo.setGuildId(player.getGuildId());
        playerInfo.setGuildName(player.gainGuildName());
        playerInfo.setGuildPost(player.gainGuildPost());
        playerInfo.setTitle(player.getTitleData().getWearId());
        if (player.getCurHuaxinEntity() != null) {
            playerInfo.setFeijianId(player.getCurHuaxinEntity().getExcelId());
            playerInfo.setFeijianUid(player.getCurHuaxinEntity().getId());
        }

        playerInfo.setIsGather(EntityState.Pick.compare(player.getState()));

        PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
        //TODO 结婚对象名称
        if (marryTarget != null) {
            playerInfo.setMarriageOtherName(marryTarget.getRolename());
        } else {
            playerInfo.setMarriageOtherName("");
        }
        MarryChild child = Utils.findOne(player.getChilds().values(), c -> c.getShow() > 0);
        if (child != null && child.getName() != null){
            playerInfo.setChildName(child.getName());
        }

        playerInfo.setOnSitting(EntityState.Sitting.compare(player.getState()));
        playerInfo.setCamp(player.getCamp());
        playerInfo.setShiHaiCfgId(player.getShiHaiData().getCfgId());
        playerInfo.setServerId(getServerId(player));
        playerInfo.setCurWakan(player.getCurWakan());
        playerInfo.setMaxWakan(player.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan));
        for (Buff buff : player.getBuffs()) {
            CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
            playerInfo.addBuffList(builder);
        }
        playerInfo.setSoulSpirte1(0);
        playerInfo.setSoulSpirte2(0);
        playerInfo.setSoulSpirte3(0);
        Map<Integer, SoulSpiritInfo> spiritMap = player.getStifleData().getSpiritMap();
        if (spiritMap.containsKey(1)) {
            playerInfo.setSoulSpirte1(100 + spiritMap.get(1).getEvolveLv());
        }
        if (spiritMap.containsKey(2)) {
            playerInfo.setSoulSpirte2(200 + spiritMap.get(2).getEvolveLv());
        }
        if (spiritMap.containsKey(3)) {
            playerInfo.setSoulSpirte3(300 + spiritMap.get(3).getEvolveLv());
        }
        playerInfo.setFacade(getFacade(player));


        playerInfo.setHead(getHead(player));


        return playerInfo;
    }

    public static int getServerId(Player player) {
        if (GameServer.getInstance().IsFightServer()) {
            return FightClientManager.getServerIdInFightServer(player);
        } else {
            return player.getCurServerId();
        }
    }

    //获取地图怪物信息
    public static CommonMessage.MonsterInfo.Builder getMonsterInfo(Monster monster) {
        CommonMessage.MonsterInfo.Builder monsterInfo = CommonMessage.MonsterInfo.newBuilder();
        monsterInfo.setId(monster.getId());
        monsterInfo.setDataID(monster.getModelId());
        monsterInfo.setX(monster.gainX());
        monsterInfo.setY(monster.gainY());
        monsterInfo.setCurHp(monster.getCurHp());
        monsterInfo.setMaxHp(monster.getAttribute().MaxHP());
        monsterInfo.setMoveSpeedFinal(monster.gainFinalMoveSpeed());
        monsterInfo.setAttackSpeedFinal((int) monster.getAttribute().getAdditionValue(AttributeType.AttackSpeedFinal));//player.getAttribute().gainFinalAttackSpeed());
        monsterInfo.setFightState(monster.getFightState());
        monsterInfo.setCamp(monster.getCamp());
        monsterInfo.setIsRun(EntityState.Run.compare(monster.getState()));

        if (monster.getRoads() != null) {
            for (Position pos : monster.getRoads()) {
                monsterInfo.addPosList(MapUtils.getPos(pos));
            }
        }
        if (monster.getDir() == null) {
            monsterInfo.setDirX(0);
            monsterInfo.setDirY(1);
        } else {
            monsterInfo.setDirX(monster.getDir().getX());
            monsterInfo.setDirY(monster.getDir().getY());
        }
        monsterInfo.setArmor(monster.getArmor());
        monsterInfo.setIsbirth(TimeUtils.Time() < monster.getBrithProtect());
        monsterInfo.addAllDropUserIds(monster.getDropRoleIds());

        for (Buff buff : monster.getBuffs()) {
            CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
            monsterInfo.addBuffList(builder);
        }
        return monsterInfo;
    }

    //获取采集物物信息
    public static CommonMessage.GatherInfo.Builder getGatherInfo(Gather gather) {
        CommonMessage.GatherInfo.Builder gatherInfo = CommonMessage.GatherInfo.newBuilder();
        gatherInfo.setGatherId(gather.getId());
        gatherInfo.setDataID(gather.getModelId());
        gatherInfo.setMapId(gather.getMapModelId());
        gatherInfo.setX(gather.gainCurPos().getX());
        gatherInfo.setY(gather.gainCurPos().getY());
        return gatherInfo;
    }

    //获取地图NPC信息
    public static CommonMessage.NpcInfo.Builder getNpcInfo(Npc npc) {
        CommonMessage.NpcInfo.Builder info = CommonMessage.NpcInfo.newBuilder();
        info.setNpcId(npc.getId());
        info.setDataID(npc.getModelId());
        info.setX(npc.gainCurPos().getX());
        info.setY(npc.gainCurPos().getY());
        info.setDir(npc.getNpcDir());
        return info;
    }

    //获取地图pet信息
    public static CommonMessage.PetInfo.Builder getPetInfo(Pet pet) {
        CommonMessage.PetInfo.Builder info = CommonMessage.PetInfo.newBuilder();
        info.setId(pet.getId());
        info.setDataID(pet.getModelId());
        info.setOwnerID(pet.getOwnerId());
        info.setLevel(pet.getLevel());
        Player player = Manager.playerManager.getPlayerCache(pet.getOwnerId());
        if (player != null) {
            info.setOwnerName(player.getName());
            info.setCamp(player.getCamp());
        } else {
            info.setOwnerName(pet.getOwnerName());
            info.setCamp(pet.getCamp());
        }
        info.setSpeed(pet.gainFinalMoveSpeed());
        info.setX(pet.gainCurPos().getX());
        info.setY(pet.gainCurPos().getY());
        info.setCurHp(pet.getCurHp());
        info.setMaxHp(pet.getAttribute().MaxHP());
        info.setFightState(pet.getFightState());
        for (Buff buff : pet.getBuffs()) {
            CommonMessage.Buff.Builder builder = Manager.buffManager.deal().build(buff);
            info.addBuffList(builder);
        }
        return info;
    }

    //构建地图墓碑信息
    public static CommonMessage.TombstoneInfo.Builder getTombstoneInfo(Tombstone tombstone) {
        CommonMessage.TombstoneInfo.Builder info = CommonMessage.TombstoneInfo.newBuilder();
        info.setId(tombstone.getId());
        info.setStoneId(tombstone.getMonsterId());
        info.setX(tombstone.gainCurPos().getX());
        info.setY(tombstone.gainCurPos().getY());
        info.setCd((int) (tombstone.getDieTime() > TimeUtils.Time() ? tombstone.getDieTime() - TimeUtils.Time() : 0) / 1000);
        return info;
    }

    //获取网络pos
    public static CommonMessage.Position.Builder getPos(Position pos) {
        CommonMessage.Position.Builder nPos = CommonMessage.Position.newBuilder();
        nPos.setX(pos.getX());
        nPos.setY(pos.getY());
        return nPos;
    }

    /**
     * 寻路算法
     *
     * @param map
     * @param start 开始节点
     * @param end   结束节点
     * @param steps 计算步数
     * @return
     */
    public static List<Position> findRoads(MapObject map, Position start, Position end, int steps) {
        //返回的移动路径
        List<Position> result = new ArrayList<>();
        if (start.compare(end)) {
            result.add(end);
            return result;
        }

        RoadPoint startRoad = new RoadPoint();
        startRoad.setPos(start);

        RoadPoint endRoad = new RoadPoint();
        endRoad.setPos(end);

        //待计算路点
        List<RoadPoint> waitting = new ArrayList<>();
        //已计算路点
        HashMap<Integer, RoadPoint> counted = new HashMap<>();
        //遍历过的路点索引
        HashSet<Integer> passed = new HashSet<>();

        waitting.add(startRoad);
        passed.add(getIndex(map, start));

        int step = 0;

        while (waitting.size() > 0 && (step < steps || steps == -1)) {
            //取出优先级最高的路点(权值最小)
            RoadPoint road = waitting.remove(0);
            step++;

            //到达终点
            if (road.getPos().compare(end)) {
                endRoad = road;
                break;
            }
            //加入已计算的路点
            counted.put(getIndex(map, road.getPos()), road);

            //获取周围格子信息
            List<Position> rounds = getRoundGrid(road.getPos(), 1);

            for (Position round : rounds) {
                //已经遍历过
                if (passed.contains(getIndex(map, round))) {
                    continue;
                }
                //在地图内是不可行走点
                if (!Utils.isCanMove(map, round)) {
                    continue;
                }

                RoadPoint roundPoint = new RoadPoint();
                roundPoint.setPos(round);
                roundPoint.setFarther(getIndex(map, road.getPos()));
                //加入遍历过格子
                passed.add(getIndex(map, round));
                //计算权值
                roundPoint.setWeight(countWeight(round, end));
                //插入到待计算列表
                insert(waitting, roundPoint);
            }
        }

        //计算路径
        if (endRoad.getFarther() != -1) {
            //已经找到终点
            RoadPoint _node = endRoad;
            result.add(0, end);
            while (_node.getFarther() != -1) {
                _node = counted.get(_node.getFarther());
                result.add(0, _node.getPos());
            }
        } else if (step == steps && waitting.size() > 0) {
            //到达寻路最大步数
            RoadPoint _node = waitting.get(0);
            result.add(0, _node.getPos());
            while (_node.getFarther() != -1) {
                _node = counted.get(_node.getFarther());

                result.add(0, _node.getPos());
            }
        }

        return result;
    }

    /**
     * 获取点在地图中的索引
     *
     * @param map
     * @param pos
     * @return
     */
    public static int getIndex(MapObject map, Position pos) {
        return map.getColCellCount() * (int) pos.getY() + (int) pos.getX();
    }

    /**
     * 获得周围的格子
     *
     * @param pos
     * @param radius
     * @return
     */
    public static List<Position> getRoundGrid(Position pos, int radius) {
        List<Position> points = new ArrayList<>();
        //左边界
        int left = (int) pos.getX() - radius;
        left = left > 0 ? left : 0;
        //右边界
        int right = (int) pos.getX() + radius;
        //下边界
        int down = (int) pos.getY() - radius;
        down = down > 0 ? down : 0;
        //上边界
        int up = (int) pos.getY() + radius;

        for (int i = left; i <= right; i++) {
            for (int j = down; j <= up; j++) {
                points.add(MapManager.getPos(i, j));
            }
        }
        return points;
    }

    /**
     * 计算权值 曼哈顿方法
     *
     * @param start 开始格子
     * @param end   结束给子
     * @return
     */
    private static int countWeight(Position start, Position end) {
        return (int) (Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY()));
    }

    /**
     * 按权值插入队列
     *
     * @param waitting 队列
     * @param road     路点
     */
    private static void insert(List<RoadPoint> waitting, RoadPoint road) {

        if (waitting.isEmpty()) {
            waitting.add(road);
            return;
        }

        //头部插入
        for (int i = 0; i < waitting.size(); i++) {
            RoadPoint _temp = waitting.get(i);
            if (_temp.getWeight() < road.getWeight()) {
                continue;
            }
            waitting.add(i, road);
            return;
        }
        waitting.add(road);
    }

    //同步buff改变血量
    public static void sendBuffHp(Fighter source, Fighter owner, int value) {
        BuffMessage.ResHpAddOrDec.Builder msg = BuffMessage.ResHpAddOrDec.newBuilder();
        msg.setOwnId(owner.getId());
        msg.setValue(value);
        if (owner instanceof Player) {
            MessageUtils.send_to_player((Player) owner, BuffMessage.ResHpAddOrDec.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        if (source.equals(owner)) {
            return;
        }
        if (source instanceof Player) {
            MessageUtils.send_to_player((Player) source, BuffMessage.ResHpAddOrDec.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

    }

    public static void sendExpRate(Player player) {
        PlayerMessage.ResUpdataExpRate.Builder msg = PlayerMessage.ResUpdataExpRate.newBuilder();
        msg.setExpRate(player.gainExpRate());
        MessageUtils.send_to_player(player, PlayerMessage.ResUpdataExpRate.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //同步战斗状态
    public static void sendFightState(Fighter owner) {
        ResUpdateFightState.Builder msg = ResUpdateFightState.newBuilder();
        msg.setId(owner.getId());
        msg.setState(owner.getFightState());
        MessageUtils.send_to_roundPlayer(owner, ResUpdateFightState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //同步速度
    public static void synMoveSpeed(Entity object) {

        ResMoveSpeedChange.Builder msg = ResMoveSpeedChange.newBuilder();
        msg.setObjectId(object.getId());
        msg.setValue(object.gainFinalMoveSpeed());
        MessageUtils.send_to_roundPlayer(object, ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static boolean isLevelCanEnter(Player player, int mapID) {
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapID);
        boolean min = (player.getLevel() >= config.getLevel_min()) || (config.getLevel_min() == -1);
        boolean max = (player.getLevel() <= config.getLevel_max()) || (config.getLevel_max() == -1);
        return min && max;
    }

    public static CommonMessage.GroundBuffMess.Builder getGroundBuffInfo(GroundBuff magic) {
        CommonMessage.GroundBuffMess.Builder msg = CommonMessage.GroundBuffMess.newBuilder();
        msg.setId(magic.getId());
        msg.setModelId(magic.getModelId());
        msg.setRadius(magic.getLogicSize());
        msg.setX(magic.gainCurPos().getX());
        msg.setY(magic.gainCurPos().getY());
        msg.setActType(magic.getType());
        return msg;
    }

    public static CommonMessage.Magic.Builder getMagicInfo(SkillMagic magic) {
        CommonMessage.Magic.Builder msg = CommonMessage.Magic.newBuilder();
        msg.setId(magic.getId());
        msg.setModelId(magic.getMagicIndex());
        msg.setDelay(magic.getDelay());
        msg.setRadius(magic.getRadius());
        msg.setX(magic.gainX());
        msg.setY(magic.gainY());
        msg.setOwnerId(magic.getOwnerId());
        msg.setMoveSpeedFinal(magic.gainFinalMoveSpeed());
        return msg;
    }

    public static CommonMessage.FacadeAttribute.Builder getFacade(Player player) {
        int haloID = Manager.immortalEquipManager.manager().getImmFacadeForType(player, 32);
        int matrixID = Manager.immortalEquipManager.manager().getImmFacadeForType(player, 33);
        CommonMessage.FacadeAttribute.Builder msg = CommonMessage.FacadeAttribute.newBuilder();
        msg.setWingId(player.getNewFashionData().getWingId());
        msg.setFashionBody(player.getNewFashionData().getBodyID());
        msg.setFashionHalo(haloID);
        msg.setFashionMatrix(matrixID);
        msg.setFashionWeapon(player.getNewFashionData().getWeaponID());
        msg.setSpiritId(player.getSpiritData().getSpiritId());
        msg.setSoulArmorId(player.getSoulArmor().getWearId());
        return msg;
    }

    public static CommonMessage.FacadeAttribute.Builder getFacade(PlayerWorldInfo player) {
        CommonMessage.FacadeAttribute.Builder msg = CommonMessage.FacadeAttribute.newBuilder();
        msg.setWingId(player.getWingId());
        msg.setFashionBody(player.getFashionBodyId());
        msg.setFashionHalo(player.getFashionHalo());
        msg.setFashionMatrix(player.getFashionMatrix());
        msg.setFashionWeapon(player.getFashionWeaponId());
        msg.setSpiritId(player.getSpiritId());
        msg.setSoulArmorId(player.getSoulArmorId());
        return msg;
    }

    public static CommonMessage.FacadeAttribute.Builder getFacade(int windId, int bodyId, int halo, int matrix, int weaponId, int spiritId, int soulArmorId) {
        CommonMessage.FacadeAttribute.Builder msg = CommonMessage.FacadeAttribute.newBuilder();
        msg.setWingId(windId);
        msg.setFashionBody(bodyId);
        msg.setFashionHalo(halo);
        msg.setFashionMatrix(matrix);
        msg.setFashionWeapon(weaponId);
        msg.setSpiritId(spiritId);
        msg.setSoulArmorId(soulArmorId);
        return msg;
    }

    public static CommonMessage.HeadAttribute.Builder getHead(Player player) {
        CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
        //@todo 头像修改

        int headId = 0;
        int headFrameId = 0;
         if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
                 headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
         }
         if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
                headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
          }
        msg.setFashionHead(headId);
        msg.setFashionFrame(headFrameId);

        //自定义头像
        if(!StringUtils.isEmpty(player.getCustomHeadPath())){
            msg.setCustomHeadPath(player.getCustomHeadPath());
        }
        msg.setUseCustomHead(player.isUseCustomHead());
        return msg;
    }

    public static CommonMessage.HeadAttribute.Builder getHead(PlayerWorldInfo player) {
        CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
        //@todo 头像修改
        msg.setFashionHead(player.getFashionHeadId());
        msg.setFashionFrame(player.getFashionHeadFrameId());
        //自定义头像
        if(!StringUtils.isEmpty(player.getCustomHeadPath())){
            msg.setCustomHeadPath(player.getCustomHeadPath());
        }
        msg.setUseCustomHead(player.isUseCustomHead());
        return msg;
    }

    public static CommonMessage.HeadAttribute.Builder getHead(int fashionHead, int fashionHeadFrameId, String customHeadPath, boolean isUseCustomHead) {
        CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
        //@todo 头像修改
        msg.setFashionHead(fashionHead);
        msg.setFashionFrame(fashionHeadFrameId);
        //自定义头像
        if(!StringUtils.isEmpty(customHeadPath)){
            msg.setCustomHeadPath(customHeadPath);
        }
        msg.setUseCustomHead(isUseCustomHead);
        return msg;
    }
    public static CommonMessage.HeadAttribute.Builder getDefaultHead() {
        CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
        //@todo 头像修改
        msg.setFashionHead(0);
        msg.setFashionFrame(0);
        //自定义头像

        //msg.setCustomHeadPath("");

        msg.setUseCustomHead(false);
        return msg;
    }
    public static void sendDead(Fighter attacker, Fighter deader) {
        MapObject map = Manager.mapManager.getMap(deader.gainMapId());
        if (attacker instanceof Pet) {
            Pet pet = (Pet) attacker;
            attacker = getFighter(map, pet.getOwnerId());
        } else if (attacker instanceof SkillMagic) {
            SkillMagic magic = (SkillMagic) attacker;
            attacker = getFighter(map, magic.getOwnerId());
        } else if (attacker instanceof HuaxinEntity) {
            HuaxinEntity entity = (HuaxinEntity) attacker;
            attacker = getFighter(map, entity.getOwnerId());
        }
        FightMessage.ResObjDead.Builder msg = FightMessage.ResObjDead.newBuilder();
        StringBuilder destStr = new StringBuilder();
        if (null == attacker) {
            msg.setTransMark(0);
            msg.setKillerName("");
        } else {
            boolean isMark = Utils.getMarkAfterString(attacker.getName(), destStr);
            msg.setTransMark(isMark ? 1 : 0);
            msg.setKillerName(destStr.toString());
        }

        long nowTime = TimeUtils.Time();
        msg.setDeaderId(deader.getId());
        setReviveType(map, deader, nowTime);
        msg.setReviveType(deader.getReviveData().getLastReviveType());
        msg.setReviveCount(deader.getReviveData().getReviveCount());
        int waitTime = deader.getReviveData().getWaitTimeCD();
        waitTime = waitTime <= 0 ? 0 : waitTime;
        msg.setReviveLastTime(waitTime);
        MessageUtils.send_to_roundPlayer(deader, FightMessage.ResObjDead.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void setReviveType(MapObject map, Fighter deader, long nowTime) {
        if (!(deader instanceof Player)) {
            return;
        }
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        Cfg_Relive_Bean relive_bean = CfgManager.getCfg_Relive_Container().getValueByKey(config.getRelive_type());
        if (relive_bean == null) {
            return;
        }
        deader.getReviveData().setLastReviveType(relive_bean.getRelive_id());
        if (relive_bean.getSitu_relive_time() > 0) {
            int baseTime = relive_bean.getSitu_relive_time();//基础时间
            int clearTime = relive_bean.getSitu_relive_recovery_time();//清除CD时间
            ReadIntegerArray addTimeArr = relive_bean.getSitu_relive_add_time();
            int addTime = baseTime;
            if (addTimeArr.size() > 0) {
                int roundAddTime = addTimeArr.get(0);
                int maxAddTime = addTimeArr.get(1);
                if (nowTime - deader.getReviveData().getLastDeadTime() >= clearTime) {
                    deader.getReviveData().setReviveCount(0);
                } else {
                    int round = deader.getReviveData().getReviveCount();
                    addTime = addTime + round * roundAddTime;
                    addTime = addTime >= maxAddTime ? maxAddTime : addTime;
                    deader.getReviveData().setReviveCount(round + 1);
                }
            }
            deader.getReviveData().setWaitTimeCD(addTime);
        } else {
            deader.getReviveData().setWaitTimeCD(0);
            deader.getReviveData().setReviveCount(0);
        }
        deader.getReviveData().setLastDeadTime(nowTime);
    }

    public static void synUpdateCamp(Fighter fighter) {
        ResUpdateCamp.Builder msg = ResUpdateCamp.newBuilder();
        msg.setId(fighter.getId());
        msg.setCamp(fighter.getCamp());
        MessageUtils.send_to_roundPlayer(fighter, ResUpdateCamp.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //强制位移到某个位置
    public static void forceMoveTo(Player player, Position pos) {
        player.changeCurPos(pos, true);
        MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
        msg.setId(player.getId());
        CommonMessage.Position.Builder mPos = CommonMessage.Position.newBuilder();
        mPos.setX(pos.getX());
        mPos.setY(pos.getY());
        msg.setTarget(mPos);
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //同步战斗状态
    public static void synPlayerFightState(Player player) {
        ResFightOrUnFight.Builder msg = ResFightOrUnFight.newBuilder();
        msg.setIsFight(player.isInBattle());
        MessageUtils.send_to_player(player, ResFightOrUnFight.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //强制位移到某个位置
    public static void synPlayerEquipMinStar(Player player) {
        ResEquipMinStar.Builder msg = ResEquipMinStar.newBuilder();
        msg.setRoleId(player.getId());
        msg.setEquipMinStar(Manager.equipManager.getClothesStar(player));
        MessageUtils.send_to_player(player, ResEquipMinStar.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        if (!player.isShowEquipStar()) {
            msg.setEquipMinStar(0);
        }
        MessageUtils.send_to_roundPlayer(player, ResEquipMinStar.MsgID.eMsgID_VALUE, msg.build().toByteArray(), false);
    }

    public static List<Player> getRoundPlayer(MapObject map, Position pos) {
        List<Player> members = new ArrayList<>();
        Iterator<Area> iter = Manager.mapManager.getRounds(map, pos).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();
            members.addAll(area.getPlayers());
        }
        return members;
    }

    public static List<Monster> getRoundMonster(MapObject map, Position pos) {
        List<Monster> members = new ArrayList<>();
        Iterator<Area> iter = Manager.mapManager.getRounds(map, pos).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();
            Iterator<Monster> mIter = area.getMonsters().values().iterator();
            while (mIter.hasNext()) {
                Monster monster = mIter.next();
                members.add(monster);
            }
        }
        return members;
    }


    /**
     * 获取战斗对象
     *
     * @param map
     * @param id
     * @return
     */
    public static Fighter getFighter(MapObject map, long id) {
        Fighter fighter = map.getPlayer(id);
        if (fighter != null) {
            return fighter;
        }
        fighter = map.getMonster(id);
        if (fighter != null) {
            return fighter;
        }
        fighter = map.getRobots().get(id);
        if (fighter != null) {
            return fighter;
        }
        for (Player player : map.getPlayers().values()) {
            Pet pet = Manager.petManager.getBattlePet(player);
            if (pet != null && pet.getId() == id) {
                return pet;
            }
            HuaxinEntity entity = player.getCurHuaxinEntity();
            if (entity != null && entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取主人战斗对象
     *
     * @param map
     * @param fighter
     * @return
     */
    public static Fighter getOwnFighter(MapObject map, Fighter fighter) {
        if (fighter instanceof Pet) {
            Pet pet = (Pet) fighter;
            fighter = getFighter(map, pet.getOwnerId());
        } else if (fighter instanceof SkillMagic) {
            SkillMagic magic = (SkillMagic) fighter;
            fighter = MapUtils.getFighter(map, magic.getOwnerId());
            if (fighter instanceof HuaxinEntity) {
                HuaxinEntity entity = (HuaxinEntity) fighter;
                fighter = MapUtils.getFighter(map, entity.getOwnerId());
            }
        } else if (fighter instanceof HuaxinEntity) {
            HuaxinEntity entity = (HuaxinEntity) fighter;
            fighter = MapUtils.getFighter(map, entity.getOwnerId());
        } else if (fighter instanceof Robot) {
            Robot robot = (Robot) fighter;
            fighter = MapUtils.getFighter(map, robot.getOwnerId());
        }
        return fighter;
    }

    /**
     * 获取主动怪的攻击范围
     *
     * @param map
     * @param pos
     * @param radx
     * @param rady
     * @param flag 0 全部 -1 全不要 第0位1不要怪 第1位不要角色 第2位不要宠物(暂时还没有) 第3位不要机器人
     * @return
     */
    public static ArrayList<Fighter> getFighterNearNoCheck(MapObject map, Position pos, int radx, int rady, int flag) {

        ArrayList<Fighter> fighters = new ArrayList<>();
        Iterator<Area> iter = Manager.mapManager.getRoundAreasSort(map, pos, radx, rady, true).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();

            if ((flag & FighterFlag.NoMonster.v) == 0) {
                Iterator<Monster> mIter = area.getMonsters().values().iterator();
                while (mIter.hasNext()) {
                    Monster fighter = mIter.next();
                    if (fighter.isDie()) {
                        continue;
                    }
                    if (fighter.getCamp() < 1) {
                        continue;
                    }
                    fighters.add(fighter);
                }
            }

            if ((flag & FighterFlag.NoPlayer.v) == 0) {
                for (Player fighter : area.getPlayers()) {
                    if (fighter.isDie()) {
                        continue;
                    }
                    fighters.add(fighter);
                }
            }

            if ((flag & FighterFlag.NoRobot.v) == 0) {
                Iterator<Robot> rIter = area.getRobots().values().iterator();
                while (rIter.hasNext()) {
                    Robot robot = rIter.next();
                    if (robot.isDie()) {
                        continue;
                    }
                    fighters.add(robot);
                }
            }
        }

        return fighters;
    }

    /**
     * 检查怪物地图上的可攻击对象，包含死亡的玩家与机器人
     *
     * @param map  当前地图
     * @param pos  当前坐标点
     * @param radx 范围X
     * @param rady 范围Y
     * @return 返回选中的玩家与机器及宠物
     */
    public static ArrayList<Fighter> getFighterNearHaveDie(MapObject map, Position pos, int radx, int rady, int monsterModelId) {
        if (radx > map.getRound()) {
            radx = map.getRound();
        }
        if (rady > map.getRound()) {
            rady = map.getRound();
        }

        ArrayList<Fighter> fighters = new ArrayList<>();
        Iterator<Area> iter = Manager.mapManager.getRoundAreasSort(map, pos, radx, rady, true).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();
            for (Player fighter : area.getPlayers()) {
                if (fighter.isDie()) {
                    continue;
                }
                fighters.add(fighter);
            }

            Iterator<Robot> rIter = area.getRobots().values().iterator();
            while (rIter.hasNext()) {
                Robot robot = rIter.next();
                if (robot.isDie()) {
                    continue;
                }
                fighters.add(robot);
            }
        }
        return fighters;
    }

    /**
     * 获取主动怪的攻击范围
     *
     * @param map
     * @param pos
     * @param radx
     * @param rady
     * @param flag 0 全部 -1 全不要 第0位1不要怪 第1位不要角色 第2位不要宠物(暂时还没有) 第3位不要机器人
     * @return
     */
    public static ArrayList<Fighter> getFighterNear(MapObject map, Position pos, int radx, int rady, int flag) {
//        if(radx > map.getRound() || rady > map.getRound()){
//            LOGGER.info(String.format("getFighterNear 找人范围过大,本地图视野直径:%d 将搜索范围改为以视野直径作为半径 radx:%d rady:%d", map.getRound(), radx, rady));
//        }
        if (radx > map.getRound()) {
            radx = map.getRound();
        }
        if (rady > map.getRound()) {
            rady = map.getRound();
        }
        return getFighterNearNoCheck(map, pos, radx, rady, flag);
    }

    //获取周围战斗对象
    public static List<Fighter> getFighter(MapObject map, Position pos) {
        List<Fighter> fighters = new ArrayList<>();
        Iterator<Area> iter = Manager.mapManager.getRounds(map, pos).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();
            Iterator<Monster> mIter = area.getMonsters().values().iterator();
            while (mIter.hasNext()) {
                Monster fighter = mIter.next();
                if (fighter.isDie()) {
                    continue;
                }
                if (fighter.getCamp() < 1) {
                    continue;
                }
                fighters.add(fighter);
            }

            for (Player fighter : area.getPlayers()) {
                if (fighter.isDie()) {
                    continue;
                }

                fighters.add(fighter);
            }

            Iterator<Robot> rIter = area.getRobots().values().iterator();
            while (rIter.hasNext()) {
                Robot robot = rIter.next();
                if (robot.isDie()) {
                    continue;
                }
                fighters.add(robot);
            }
        }

        return fighters;

    }

    //获取周围战斗对象
    public static List<Fighter> getFighter(MapObject map, Position pos, float radius, int flag) {
        ArrayList<Fighter> fighters = new ArrayList<>();
//        if(radx > map.getRound() || rady > map.getRound()){
//            LOGGER.info(String.format("getFighterNear 找人范围过大,本地图视野直径:%d 将搜索范围改为以视野直径作为半径 radx:%d rady:%d", map.getRound(), radx, rady));
//        }
        int radx = (int) (radius / map.getArea_width()) + 1;
        int rady = (int) (radius / map.getArea_high()) + 1;

        if (radx > map.getRound()) {
            radx = map.getRound();
        }
        if (rady > map.getRound()) {
            rady = map.getRound();
        }

        // 先找到合适的格子,然后再对应的类型里面判断距离
        Iterator<Area> iter = Manager.mapManager.getRoundAreasSort(map, pos, radx, rady, true).iterator();
        while (iter.hasNext()) {
            Area area = iter.next();

            if ((flag & FighterFlag.NoMonster.v) == 0) {
                Iterator<Monster> mIter = area.getMonsters().values().iterator();
                while (mIter.hasNext()) {
                    Monster fighter = mIter.next();
                    if (fighter.isDie()) {
                        continue;
                    }
                    if (fighter.getCamp() < 1) {
                        continue;
                    }
                    if (Utils.getDistance(fighter.gainCurPos(), pos) > radius) {
                        continue;
                    }
                    fighters.add(fighter);
                }
            }

            if ((flag & FighterFlag.NoPlayer.v) == 0) {
                for (Player fighter : area.getPlayers()) {
                    if (fighter.isDie()) {
                        continue;
                    }
                    if (Utils.getDistance(fighter.gainCurPos(), pos) > radius) {
                        continue;
                    }
                    fighters.add(fighter);
                }
            }

            if ((flag & FighterFlag.NoRobot.v) == 0) {
                Iterator<Robot> rIter = area.getRobots().values().iterator();
                while (rIter.hasNext()) {
                    Robot robot = rIter.next();
                    if (robot.isDie()) {
                        continue;
                    }
                    if (Utils.getDistance(robot.gainCurPos(), pos) > radius) {
                        continue;
                    }
                    fighters.add(robot);
                }
            }
        }

        return fighters;
    }

    public static void OnBreakPack(Player player) {
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Gather);
        MapMessage.ResEndGather.Builder msg = MapMessage.ResEndGather.newBuilder();
        msg.setRoleId(player.getId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResEndGather.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    public static void sendChangeMap(Player player, int mapModelId, int line, Position pos, int reson, int type, long param) {
        MapMessage.ResEnterMap.Builder msg = MapMessage.ResEnterMap.newBuilder();
        msg.setResult(reson);
        msg.setMapDataID(mapModelId);
        msg.setLine(line);
        msg.setPos(MapUtils.getPos(pos));
        msg.setParam((int) param);
        msg.setType(type);
        MessageUtils.send_to_player(player, MapMessage.ResEnterMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void sendPlayerLoadingMapID(Player player) {
        RegisterMessage.ResPlayerMapInfo.Builder msg = RegisterMessage.ResPlayerMapInfo.newBuilder();
        msg.setMapId(player.gainMapModelId());
        msg.setLineId(player.gainLine());
        msg.setRoleId(player.getId());
        msg.setX(player.gainCurPos().getX());
        msg.setZ(player.gainCurPos().getY());
        MessageUtils.send_to_player(player, RegisterMessage.ResPlayerMapInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void sendPlayerLoadingMapID(Player player, int mapID, int line, long roleID, Position pos) {
        RegisterMessage.ResPlayerMapInfo.Builder msg = RegisterMessage.ResPlayerMapInfo.newBuilder();
        msg.setMapId(mapID);
        msg.setLineId(line);
        msg.setRoleId(roleID);
        msg.setX(pos.getX());
        msg.setZ(pos.getY());
        MessageUtils.send_to_player(player, RegisterMessage.ResPlayerMapInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void sendHpChange(Entity entity) {
        if (entity.getCurHp() <= 0) {
            return;
        }
        FightMessage.ResHpChange.Builder msg = FightMessage.ResHpChange.newBuilder();
        msg.setOwnId(entity.getId());
        msg.setCurHp(entity.getCurHp());
        MessageUtils.send_to_roundPlayer(entity, FightMessage.ResHpChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public static void sendWakanChange(Entity entity) {
        FightMessage.ResWakanChange.Builder msg = FightMessage.ResWakanChange.newBuilder();
        msg.setOwnId(entity.getId());
        msg.setCurWakan(entity.getCurWakan());
        msg.setMaxWakan(entity.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan));
        MessageUtils.send_to_roundPlayer(entity, FightMessage.ResWakanChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //TODO 广播地图特效
    public static void notifyEffect(int type) {
        MapMessage.ResPlayEffect.Builder effect = MapMessage.ResPlayEffect.newBuilder();
        effect.setEffectType(type);
        MessageUtils.send_to_all_player(MapMessage.ResPlayEffect.MsgID.eMsgID_VALUE, effect.build().toByteArray());
    }

    //审核服特殊处理延长一个月
    public static long getEndTime(int time) {
        long now = TimeUtils.Time();
        long monthTime = 30 * GlobalType.MILLIS_PER_DAY;
        return ServerConfig.getIsShenHe() > 0 ? now + monthTime : now + time * 1000L;
    }


}
