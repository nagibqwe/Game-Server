package common.map;

import com.data.CfgManager;
import com.data.bean.Cfg_Gather_Bean;
import com.data.bean.Cfg_GroundBuff_Bean;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.DirMoveBehavior;
import com.game.behavior.structs.type.GatherBehavior;
import com.game.behavior.structs.type.JumpBehavior;
import com.game.behavior.structs.type.MoveBehavior;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.manager.Manager;
import com.game.map.script.IMapHandler;
import com.game.map.structs.GroundBuff;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapUtils;
import  com.data.MessageString;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.MapMessage;
import game.message.MapMessage.ReqJump;
import game.message.MapMessage.ReqJumpBlock;
import game.message.MapMessage.ReqMoveTo;
import game.message.MapMessage.ResGroundBuffStar;
import game.message.MapMessage.ResJump;
import game.message.MapMessage.ResJumpDown;
import game.message.MapMessage.ResNotCanGather;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地图的协议处理接口类
 *
 * @author admin
 */
public class MapDealScript implements IScript, IMapHandler {

    private static final Logger log = LogManager.getLogger("MapDealScript");

    @Override
    public int getId() {
        return ScriptEnum.MapDealBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnReqDirMove(Player player, MapMessage.ReqDirMove messInfo) {
        if (!isDealThisRequest(player)) {
            return;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在眩晕中， 不可移动！");
            return;
        }
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在定身中， 不可移动！");
            return;
        }
        if (player.gainMapModelId() != messInfo.getMapId()) {
            return;
        }
        //清理出生保护
       // player.setBrithProtect(0);
        //清理追击点
        player.getDirs().clear();

        player.setMoveDir(null);

        //清理old移动行为
        if (EntityState.Move.compare(player.getState())) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);
        }

        if (EntityState.Jump.compare(player.getState())) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Jump);
        }
        
        //移动停止采集
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Gather);
        
        Position clientPos = new Position(messInfo.getCurPos().getX(), messInfo.getCurPos().getY());
        float dis = Utils.getDistance(player.gainCurPos(), clientPos);
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        //误差修正
        if (dis > MapDefine.MAX_ERROR_DISTANCE) {
            Manager.mapManager.synStopMove(player, true);
            log.error("玩家移动误差=" + dis + "clientPos" + clientPos + player);
            return;
        }
        if (!EntityState.Fly.compare(player.getState())) {
            if (!Utils.isCanMove(map, player.gainCurPos(), clientPos)) {
                Manager.mapManager.synStopMove(player, true);
                log.info("玩家移动阻挡点=" + dis + "clientPos" + clientPos + player);
                return;
            }
        }

        if (EntityState.Dead.compare(player.getState())) {
            Fighter kill = player.gainCurAttackTarget(map);
           // MapUtils.sendDead(kill, player);
            Manager.mapManager.synStopMove(player, true);
            return;
        }

        //设置方向移动信息
        Position dir = new Position(messInfo.getDir().getX(), messInfo.getDir().getY());
        player.setMoveDir(dir);
        player.setDir(dir);
        player.changeCurPos(clientPos, true);
        //广播消息移动
        MapMessage.ResDirMove.Builder dirMoveMsg = MapMessage.ResDirMove.newBuilder();
        dirMoveMsg.setObjectId(player.getId());
        dirMoveMsg.setDir(messInfo.getDir());
        dirMoveMsg.setCurPos(messInfo.getCurPos());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResDirMove.MsgID.eMsgID_VALUE, dirMoveMsg.build().toByteArray(), false);

        BehaviorManager.InsertOnlyBehavior(player, new DirMoveBehavior(player));
        player.addState(EntityState.Move);

//        log.error("方向移动oldPos:"+oldPos + "curPos:" + player.gainCurPos()+ "方向：" + dir);
    }

    //获取地图线路
    @Override
    public void OnReqGetLines(Player player, MapMessage.ReqGetLines messInfo) {
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(player)) {
            log.error(player.nameIdString() + " 正在定身中， 不可变线！");
            return;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(player)) {
            log.error(player.nameIdString() + " 正在眩晕中， 不可变线！");
            return;
        }

        MapMessage.ResLineList.Builder send = MapMessage.ResLineList.newBuilder();

        for (MapObject mapObject : Manager.mapManager.getWorldMaps().get(player.gainMapModelId())) {
            send.addLines(mapObject.getLineId());

        }
        MessageUtils.send_to_player(player, MapMessage.ResLineList.MsgID.eMsgID_VALUE, send.build().toByteArray());
    }

    private void notCanReqGather(Player player, long gatherId) {
        ResNotCanGather.Builder msg = ResNotCanGather.newBuilder();
        msg.setGatherId(gatherId);
        MessageUtils.send_to_player(player, ResNotCanGather.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //采集
    @Override
    public void OnReqGather(Player player, MapMessage.ReqGather messInfo) {
        if (Manager.buffManager.deal().haveDing(player)) {
            log.error(player.nameIdString() + " 正在定身中， 不可采集！");
            notCanReqGather(player, messInfo.getId());
            return;
        }
        if (Manager.buffManager.deal().haveDizziness(player)) {
            log.error(player.nameIdString() + " 正在眩晕中， 不可采集！");
            notCanReqGather(player, messInfo.getId());
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            notCanReqGather(player, messInfo.getId());
            return;
        }

        if (player.isDie()) {
            notCanReqGather(player, messInfo.getId());
            return;
        }

        Gather gather = map.getGather(messInfo.getId());

        if (null == gather) {
            notCanReqGather(player, messInfo.getId());
            return;
        }

        Cfg_Gather_Bean config = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == config) {
            notCanReqGather(player, messInfo.getId());
            return;
        }
        if (config.getScriptId() > 0) {
            IScript is = Manager.scriptManager.GetScriptClass(config.getScriptId());
            if (is instanceof ICopyGatherScript) {
                ICopyGatherScript icgs = (ICopyGatherScript) is;
                if (!icgs.onBeginGather(player, gather)) {
                    log.error(player + " 采集" + gather.getName() + "（" + gather.getId() + "） 在脚本（" + config.getScriptId() + "）中返回失败了，不能采集！");
                    notCanReqGather(player, messInfo.getId());
                    return;
                }
            } else {
                log.error("gatherCfg.getScriptId()" + config.getScriptId() + "指定的脚本并不是采集脚本");
            }
        }

        //取消上次的采集
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Gather);
        //去掉隐身BUFF
        Manager.buffManager.deal().removeBuffInvisible(player);

        MapMessage.ResBeginGather.Builder msg = MapMessage.ResBeginGather.newBuilder();
        msg.setRoleId(player.getId());
        msg.setGatherId(gather.getId());
        msg.setX(player.gainCurPos().getX());
        msg.setY(player.gainCurPos().getY());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResBeginGather.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        player.addState(EntityState.Pick);
        GatherBehavior behavior = new GatherBehavior(player);
        behavior.setTargetId(gather.getId());
        behavior.setModelId(gather.getModelId());
        behavior.setEnd(TimeUtils.Time() + config.getCollect_time());
        BehaviorManager.InsertBehavior(player, behavior);
        log.info("开始采集=" + gather + player.nameIdString());
    }

    //获取怪物坐标
    @Override
    public void OnReqGetMonsterPos(Player player, MapMessage.ReqGetMonsterPos messInfo) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }

        if (map.getId() == 0) {
            return;
        }

        if (map.getMonsters().isEmpty()) {
            return;
        }

        for (Map.Entry<Long, Monster> entry : map.getMonsters().entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue().getCamp() == player.getCamp()) {
                continue;
            }
            MapMessage.ResMonsterPos.Builder send = MapMessage.ResMonsterPos.newBuilder();
            send.setX(entry.getValue().gainX());
            send.setY(entry.getValue().gainY());
            MessageUtils.send_to_player(player, MapMessage.ResMonsterPos.MsgID.eMsgID_VALUE, send.build().toByteArray());
            return;
        }
    }

    //跳出阻挡
    @Override
    public void OnReqJumpBlock(Player player, ReqJumpBlock messInfo) {
        player.setBrithProtect(0);
        Position target = new Position(messInfo.getTarget().getX(), messInfo.getTarget().getY());
        if (EntityState.ChangeMap.compare(player.getState())) {
            Manager.mapManager.synStopMove(player, true);
            return;
        }

        float distanceOffset = Utils.getDistance(target, player.gainCurPos());
        if (distanceOffset >= 10.0) {
            return;
        }
        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.JumpBlock, null)) {
            long remainTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.JumpBlock, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, String.valueOf(remainTime / 1000));
            return;
        }

        Manager.cooldownManager.addCooldown(player, CooldownTypes.JumpBlock, null, 180000);
        player.changeCurPos(target, true);
        MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
        msg.setId(player.getId());
        msg.setTarget(MapUtils.getPos(target));
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    //移动
    @Override
    public void OnReqMoveTo(Player player, ReqMoveTo messInfo) {
        if (!isDealThisRequest(player)) {
            // LOGGER.error(TaskHelp.getPlayerInfo(player) + " 的请求不被受理！");
            return;
        }
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在定身中， 不可移动！");
            return;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在眩晕中， 不可采集！");
            return;
        }
        //非本地图的移动消息抛弃
        if (player.gainMapModelId() != messInfo.getMapId()) {
            return;
        }
        //清理出生保护
        //player.setBrithProtect(0);
        //清理追击点
        player.getDirs().clear();

        Position clientPos = new Position(messInfo.getCurPos().getX(), messInfo.getCurPos().getY());

        //清理old移动行为
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Gather);
        if (EntityState.Jump.compare(player.getState())) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Jump);
        }

        float dis = Utils.getDistance(player.gainCurPos(), clientPos);
        //误差修正
        if (dis > MapDefine.MAX_ERROR_DISTANCE) {
            Manager.mapManager.synStopMove(player, true);
            log.error("玩家移动误差=" + dis + " server=" + player.gainCurPos() + "client=" + clientPos + player);
            return;
        }

        if (EntityState.Dead.compare(player.getState())) {

            MapUtils.sendDead(null, player);
            Manager.mapManager.synStopMove(player, true);
            return;
        }

        //组建新的移动信息
        List<Position> roads = new ArrayList<>();
        roads.add(clientPos);

        for (CommonMessage.Position pos : messInfo.getPosListList()) {
            roads.add(new Position(pos.getX(), pos.getY()));
        }

        player.changeCurPos(clientPos, true);
        player.setRoads(roads);
        player.addState(EntityState.Move);
        //构建移动消息
        MapMessage.ResMoveTo.Builder moveMsg = MapMessage.ResMoveTo.newBuilder();
        moveMsg.setObjectId(player.getId());
        for (Position pos : roads) {
            moveMsg.addPosList(MapUtils.getPos(pos));
        }
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), false);

        //插入移动行为
        BehaviorManager.InsertBehavior(player, new MoveBehavior(player));

//        log.error("玩家移动  player={}"  , player);
    }

    @Override
    public void OnReqPetMoveTo(Player player, MapMessage.ReqPetMoveTo messInfo) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(pet)) {
            Manager.mapManager.synStopMove(pet, true);
            log.error("出战宠物正在定身中，不可移动！player = " + player.getId());
            return;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(pet)) {
            Manager.mapManager.synStopMove(pet, true);
            log.error("出战宠物正在眩晕中，不可移动！player = " + player.getId());
            return;
        }
        //非本地图的移动消息抛弃
        if (pet.gainMapModelId() != messInfo.getMapId()) {
            return;
        }
        //清理追击点
        pet.getDirs().clear();

        Position clientPos = new Position(messInfo.getCurPos().getX(), messInfo.getCurPos().getY());

        //清理old移动行为
        BehaviorManager.CancelBehaviorByType(pet, BehaviorType.Move);
        BehaviorManager.CancelBehaviorByType(pet, BehaviorType.DirMove);

//        log.info("出战宠物移动，当前位置 = " + pet.gainCurPos() + "，客户端传输位置 = " + clientPos);
        //组建新的移动信息
        List<Position> roads = new ArrayList<>();
        roads.add(clientPos);

        for (CommonMessage.Position pos : messInfo.getPosListList()) {
            roads.add(new Position(pos.getX(), pos.getY()));
        }

        pet.changeCurPos(clientPos, true);
        pet.setRoads(roads);
        pet.addState(EntityState.Move);
        //构建移动消息
        MapMessage.ResMoveTo.Builder moveMsg = MapMessage.ResMoveTo.newBuilder();
        moveMsg.setObjectId(pet.getId());
        for (Position pos : roads) {
            moveMsg.addPosList(MapUtils.getPos(pos));
        }
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), false);

        //插入移动行为
        BehaviorManager.InsertBehavior(pet, new MoveBehavior(pet));
    }

    private boolean isDealThisRequest(Player player) {
        return !player.getHorse().isRideOther();
    }

    //复活
    @Override
    public void OnReqRelive(Player player, MapMessage.ReqRelive messInfo) {

        ReliveType type = ReliveType.CurPos.compareTo(messInfo.getType()) ? ReliveType.CurPos : ReliveType.GoBack;

        Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, type, messInfo.getIsUseGold(), null);
    }

    @Override
    public void OnReqSelectLine(Player player, MapMessage.ReqSelectLine messInfo) {
        if (Manager.buffManager.deal().haveDing(player)) {
            log.error(player.nameIdString() + " 正在定身中， 不可移动！");
            return;
        }
        if (Manager.buffManager.deal().haveDizziness(player)) {
            log.error(player.nameIdString() + " 正在眩晕中， 不可选线！");
            return;
        }

        if (player.gainLine() == messInfo.getLine()) {
            return;
        }

        //战斗状态
        if (!player.getBeEnemys().isEmpty()) {
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_BATTLE, -1, -1);
            return;
        }

        //如果正在切换地图
        if (EntityState.ChangeMap.compare(player.getState())) {
            return;
        }

        //如果玩家死亡
        if (player.isDie()) {
            if (EntityState.Dead.compare(player.getState())) {
                MapUtils.sendDead(null, player);
            }
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_PLAYER_DIE, -1, -1);
            return;
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        //副本地图不能选线
        if (map.getType() == MapDefine.COPY_MAP) {
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_UN_FIND, -1, -1);
            return;
        }

        Manager.mapManager.changeMap(player, player.gainLine(), messInfo.getLine(), player.gainCurPos(), -1);
    }

    //停止移动
    @Override
    public void OnReqStopMove(Player player, MapMessage.ReqStopMove messInfo) {
        if (!isDealThisRequest(player)) {
            // LOGGER.error(TaskHelp.getPlayerInfo(player) + " 的请求不被受理！");
            return;
        }
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(player)) {
            log.error(player.nameIdString() + " 正在定身中， 不可移动！");
            return;
        }

        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(player)) {
//            manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在眩晕中， 不可移动！");
            return;
        }

        if (player.gainMapModelId() != messInfo.getMapId()) {
            return;
        }
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);

        if (EntityState.ChangeMap.compare(player.getState())) {
            Manager.mapManager.synStopMove(player, true);
            return;
        }

        Position pos = new Position(messInfo.getPos().getX(), messInfo.getPos().getY());
//        LOGGER.error("player:"+ player.getName() + "停止移动 serverPos: " + player.gainCurPos() + "clientPos: " + pos );
        float distanceOffset = Utils.getDistance(player.gainCurPos(), pos);
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        //如果超过最大误差距离
        if (distanceOffset > MapDefine.MAX_ERROR_DISTANCE || !Utils.isCanMove(map, player.gainCurPos(), pos)) {
            Manager.mapManager.synStopMove(player, true);
            return;
        }
        player.changeCurPos(pos, true);

        Manager.mapManager.synStopMove(player, false);

    }

    @Override
    public void OnReqPetStopMove(Player player, MapMessage.ReqPetStopMove messInfo) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(pet)) {
            log.error("出战宠物正在定身中，不可移动！player = " + player.getId());
            return;
        }

        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(pet)) {
            log.error("出战宠物正在眩晕中，不可移动！player = " + player.getId());
            return;
        }

        if (pet.gainMapModelId() != messInfo.getMapId()) {
            return;
        }
        if (EntityState.ChangeMap.compare(pet.getState())) {
            Manager.mapManager.synStopMove(pet, true);
            return;
        }

        Position pos = new Position(messInfo.getPos().getX(), messInfo.getPos().getY());
//        LOGGER.error("player:"+ player.getName() + "停止移动 serverPos: " + player.gainCurPos() + "clientPos: " + pos );
        float distanceOffset = Utils.getDistance(pet.gainCurPos(), pos);
        MapObject map = Manager.mapManager.getMap(pet.gainMapId());
        //如果超过最大误差距离
        if (distanceOffset > MapDefine.MAX_ERROR_DISTANCE || !Utils.isCanMove(map, pet.gainCurPos(), pos)) {
            Manager.mapManager.synStopMove(pet, true);
            return;
        }
        pet.changeCurPos(pos, true);
        BehaviorManager.CancelBehaviorByType(pet, BehaviorType.Move);
        BehaviorManager.CancelBehaviorByType(pet, BehaviorType.DirMove);

        Manager.mapManager.synStopMove(pet, false);
    }

    //跳跃
    @Override
    public void OnReqJump(Player player, ReqJump messInfo) {
        //被攻击者处于定身状态，则不攻击
        if (Manager.buffManager.deal().haveDing(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.info(player.nameIdString() + " 正在定身中， 不可跳跃！");
            return;
        }
        //被攻击者处于眩晕状态，则不攻击
        if (Manager.buffManager.deal().haveDizziness(player)) {
            Manager.mapManager.synStopMove(player, true);
            log.error(player.nameIdString() + " 正在眩晕中， 不可跳跃！");
            return;
        }
        //LOGGER.info("起跳stage=" + messInfo.getStage() + " height=" + player.getHeight() + player);
        if (EntityState.ChangeMap.compare(player.getState())) {
            return;
        }

        if (EntityState.Move.compare(player.getState())) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);
            log.info(player.nameIdString() + " 发送跳跃时需要广播停止！");
            Manager.mapManager.synStopMove(player, true);
        }

        if (EntityState.Dead.compare(player.getState())) {
            MapUtils.sendDead(null, player);
            Manager.mapManager.synStopMove(player, true);
            return;
        }
        if (EntityState.Fly.compare(player.getState())) {
            return;
        }

//        Cfg_JumpBean config = Manager.gameDataManager.Cfg_JumpContainer.GetValueByKey(messInfo.getStage());
//        if (config == null) {
//            return;
//        }

        Position clientPos = new Position(messInfo.getCurX(), messInfo.getCurY());
        Position startTarPos = new Position(messInfo.getStartTarX(), messInfo.getStartTarY());
        Position endTarPos = new Position(messInfo.getEndTarX(), messInfo.getEndTarY());
        //移动停止采集
        BehaviorManager.CancelBehaviorByType(player, BehaviorType.Gather);
        
        //LOGGER.error("坐标呀server=" + player.gainCurPos() + "client=" + clientPos + "start=" + startTarPos + "end=" + endTarPos);
        JumpBehavior jump = (JumpBehavior) BehaviorManager.GetBehavior(player, BehaviorType.Jump);

        if (jump != null && player.getState() == 1) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Jump);
            jump = null;
        }

        //验证跳跃起点
//        if (jump == null) {
//            float jumpdis = Utils.getDistance(player.gainCurPos(), clientPos);
//            if (jumpdis > 2f && jumpdis > config.getStartMaxDis() / 100f) {
//                log.info("起跳误差stage=" + messInfo.getStage() + " clientPos=" + clientPos + " height=" + player.getHeight() + player);
//                Manager.mapManager.synStopMove(player);
//                return;
//            }
//            player.changeCurPos(clientPos, true);
//        } else {
//            float jumpTarget = Utils.getDistance(jump.getStartTarPos(), jump.getEndTarPos());
//            float jumpdis = Utils.getDistance(jump.getStartTarPos(), clientPos);
//            if (jumpdis > jumpTarget + 2 && jumpdis > config.getStartMaxDis() / 100f) {
//                log.error("起跳误差2 jumpdis=" + jumpdis + " jumpTarget=" + jumpTarget + player);
//                return;
//            }
//            player.changeCurPos(clientPos, true);
//        }
//
//        //后段
//        float dis = Utils.getDistance(startTarPos, endTarPos) * 0.9f;
//        if (dis > config.getEndMaxDis() / 100f + 0.5f) {
//            log.info("后段失败s=" + dis + player);
//            return;
//        }
//
//        //前段
//        dis = Utils.getDistance(player.gainCurPos(), startTarPos);
//        if (dis * 0.9 > config.getStartMaxDis() / 100f) {
//            log.info("前段失败s=" + dis + player);
//            return;
//        }

        if (jump == null) {
            jump = new JumpBehavior(player);
            BehaviorManager.InsertBehavior(player, jump);
            player.addState(EntityState.Jump);
        }

//        jump.setSs(dis / config.getStartTime() * 1000f);
//        jump.setSh((messInfo.getHeight() - player.getHeight()) / config.getStartTime() * 1000f);
        jump.setStart(TimeUtils.Time());
        jump.setStage(messInfo.getStage());
        jump.setHeight(messInfo.getHeight());
        jump.setStartTarPos(clientPos);
        jump.setEndTarPos(endTarPos);
        jump.setDown(false);
        jump.SetState(BehaviorStatus.ACTIVITYSTATUS_RUN);

       // player.setBrithProtect(0);
        ResJump.Builder msg = ResJump.newBuilder();
        msg.setRoleID(player.getId());
        msg.setCurX(messInfo.getCurX());
        msg.setCurY(messInfo.getCurY());
        msg.setHeight(messInfo.getHeight());
        msg.setStartTarX(messInfo.getStartTarX());
        msg.setStartTarY(messInfo.getStartTarY());
        msg.setEndTarX(messInfo.getEndTarX());
        msg.setEndTarY(messInfo.getEndTarY());
        msg.setStage(messInfo.getStage());
        MessageUtils.send_to_roundPlayer(player, ResJump.MsgID.eMsgID_VALUE, msg.build().toByteArray());

//        LOGGER.info("跳跃state=" + messInfo.getStage() + "client=" + clientPos + " head=" + jump.getStartTarPos() + " tail=" + jump.getEndTarPos() + player);
    }

    //跳跃降落
    @Override
    public void OnReqJumpDownHandler(Player player, MapMessage.ReqJumpDown messInfo) {

        JumpBehavior jump = (JumpBehavior) BehaviorManager.GetBehavior(player, BehaviorType.Jump);
        Position pos = new Position(messInfo.getTarX(), messInfo.getTarY());
        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        if (!Utils.isCanJump(map, pos)) {
            return;
        }

        if (jump == null) {
            return;
        }

        jump.setEndTarPos(pos);
        jump.SetState(BehaviorStatus.ACTIVITYSTATUS_RUN);

        ResJumpDown.Builder msg = ResJumpDown.newBuilder();
        msg.setRoleID(player.getId());
        msg.setTarX(messInfo.getTarX());
        msg.setTarY(messInfo.getTarY());

        MessageUtils.send_to_roundPlayer(player, ResJumpDown.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //行为结束
        jump.Cancel();

//        LOGGER.info("调节轻功降落点=" + pos + player);
    }

    @Override
    public void OnReqGroundBuffStar(Player player, MapMessage.ReqGroundBuffStar messInfo) {
        long gbid = messInfo.getGbid();
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            sendstarFaulire(player, 1, gbid);
            return;
        }

        GroundBuff gb = map.getGroundBuffs().get(gbid);
        if (gb == null) {
            sendstarFaulire(player, 1, gbid);
            return;
        }

        if (gb.getType() != 1) {
            log.info(player.nameIdString() + "本地图魔法是主动触发的， 不需要玩家发起！");
            sendstarFaulire(player, 2, gbid);
            return;
        }

        Cfg_GroundBuff_Bean bean = CfgManager.getCfg_GroundBuff_Container().getValueByKey(gb.getModelId());
        if (bean == null) {
            sendstarFaulire(player, 1, gbid);
            return;
        }
        float len = Utils.getDistance(player.gainCurPos(), gb.gainCurPos());
        if (len > gb.getLogicSize() + 5f) {
            sendstarFaulire(player, 2, gbid);
            return;
        }
        Manager.buffManager.deal().onAddBuff(player, player, bean.getBuff_id());
        sendstarFaulire(player, 0, gbid);
        Manager.mapManager.manager().onQuitMap(map, gb, true);
        log.error("地图BUFF " + bean.getId() + " 物体消失了！gbid=" + gb.getId());
    }

    private void sendstarFaulire(Player player, int state, long gbid) {
        ResGroundBuffStar.Builder msg = ResGroundBuffStar.newBuilder();
        msg.setGbid(gbid);
        msg.setState(state);
        MessageUtils.send_to_player(player, ResGroundBuffStar.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnReqSynPos(Player player, MapMessage.ReqSynPos messInfo) {
        Position nowPos = new Position(messInfo.getX(), messInfo.getY());
        player.getCurGps().setPos(nowPos);
//        log.error("客户端开始同步位置，客户端发送位置 = （" + messInfo.getX() + ", " + messInfo.getY() + ")。玩家当前位置 = (" +
//                    player.gainCurPos().getX() + ", " + player.gainCurPos().getY() + ")");
    }


    @Override
    public void OnReqPetJumpBlock(Player player, MapMessage.ReqPetJumpBlock messInfo) {
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet == null) {
            return;
        }
        Position target = new Position(messInfo.getTarget().getX(), messInfo.getTarget().getY());
        pet.changeCurPos(target, true);

        MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
        msg.setId(pet.getId());
        msg.setTarget(MapUtils.getPos(target));
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }

    @Override
    public void OnReqFabaoJumpBlock(Player player, MapMessage.ReqFabaoJumpBlock messInfo) {
//        Nature nature = player.getStifleData().getNature();
//        if (nature.getCurrentModelId() <= 0) {
//            return;
//        }
//        Position target = new Position(messInfo.getTarget().getX(), messInfo.getTarget().getY());
//        nature.changeCurPos(target, true);
//
//        MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
//        msg.setId(nature.getId());
//        msg.setTarget(MapUtils.getPos(target));
//        MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }
}
