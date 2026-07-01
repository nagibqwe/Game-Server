package common.map;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_State_power_Bean;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.structs.ZoneCache;
import com.game.fightserver.manager.FightClientManager;
import com.game.guildbattle.structs.GuildBattleData;
import com.game.guildbattle.structs.GuildBattleMapData;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.map.script.ITransportScript;
import com.game.map.structs.*;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.structs.Vector3;
import com.game.universe.struct.UniverseWarData;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.manager.VipManager;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.message.CrossFightMessage;
import game.message.CrossFightMessage.F2GPlayerOutCrossWorldMap;
import game.message.MapMessage;
import game.message.MapMessage.ResJumpTransport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static game.core.map.Position.ZEROPOS;

/**
 * @author admin
 */
public class TransportScript implements IScript, ITransportScript {

    private final static Logger log = LogManager.getLogger(TransportScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TransportBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    private int isCanEnter(Player player, MapObject curMap, Transport trans) {
        if (null == trans) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_UN_FIND;
        }

        float len = Utils.getDistance(player.gainCurPos(), trans.getInitPos());
        if (len > 20f) {
            log.error(player.nameIdString() + " 传递的时候，距离过大 = " + len + "了！" + trans.getInitPos());
            return MapDefine.CHANGE_MAP_RESULT_FAILED_DISTANCE_LONG;
        }

        if (!player.getBeEnemys().isEmpty()) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_BATTLE;
        }

        if (player.isDie()) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_PLAYER_DIE;
        }

        //如果正在切换地图
        if (EntityState.ChangeMap.compare(player.getState())) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_NONE;
        }

        //跳跃传送不检测剩下的
        if (trans.isJump()) {
            return MapDefine.CHANGE_MAP_RESULT_SUCCESS;
        }

        Cfg_Mapsetting_Bean targetMap = CfgManager.getCfg_Mapsetting_Container().getValueByKey(trans.getTargetID());
        if (null == targetMap) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_UN_FIND;
        }
        if (targetMap.getCan_team() == 0 && player.getTeamId() > 0) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_TEAM;
        }

        //如果传送目标是副本(副本可以用传送点到其他类型地图，但是其他类型地图不能用传送点到副本)
        if (targetMap.getType() == MapDefine.COPY_MAP && curMap.getType() != MapDefine.COPY_MAP) {
            return MapDefine.CHANGE_MAP_RESULT_FAILED_TO_COPY_MAP;
        }

        if (!MapUtils.isLevelCanEnter(player, targetMap.getMap_id())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LevelNotEnough2);
            return MapDefine.CHANGE_MAP_RESULT_FAILED_NONE;
        }

        //表示此玩家正在使用多人坐骑带人
        if (Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            StringBuilder stringBuilder = new StringBuilder();
            boolean canEnter = true;
            for (Long passenger : passengers) {
                Player passengerPlayer = Manager.playerManager.getPlayerOnline(passenger);
                if (passengerPlayer != null) {
                    if (!MapUtils.isLevelCanEnter(passengerPlayer, targetMap.getMap_id())) {
                        stringBuilder.append("[").append(passengerPlayer.getName()).append("]");
                        canEnter = false;
                    }
                }
            }
            if (!canEnter) {
                MessageUtils.notify_same_horse_palyer(player, true, Notify.ERROR, MessageString.NoticeAllHaveOneCantTransport, player.getName(), Manager.mapManager.getChatMapName(targetMap.getMap_id()), stringBuilder.toString());
                return MapDefine.CHANGE_MAP_RESULT_FAILED_NONE;
            }
        }

        return MapDefine.CHANGE_MAP_RESULT_SUCCESS;
    }

    @Override
    public void onReqTransport(Player player, int transportID) {
        log.info("客户端请求切换场景:" + player.toString() + "transportID:" + transportID);
        if (player.isHaveGuild()) {
            MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
            ZoneCache zone = mapObject.getZone();
            if (zone instanceof GuildBattleMapData) {
                GuildBattleMapData battle = (GuildBattleMapData)zone;
                GuildBattleData gbd = battle.getGuild().get(player.getGuildId());
                if (gbd != null && gbd.getCamp() == 0) {
                    return;
                }
            }
        }

        if (player.getHorse() != null && player.getHorse().isRideOther()) {
            return;
        }

        if (EntityState.ChangeMap.compare(player.getState())) {
            return;
        }

        if (player.playerCrossData.crossState >= CrossState.PCS_PIPEI) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CROSSFIGHT_ISWAIT);
            return;
        }

        if (player.playerCrossData.isCrossReqFight() || player.playerCrossData.isToFightServer()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CROSSFIGHT_ISWAIT);
            return;
        }

        if (EntityState.Move.compare(player.getState())) {
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.Move);
            BehaviorManager.CancelBehaviorByType(player, BehaviorType.DirMove);
        }

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.ChangeMap, null)) {
            return;
        }

        MapObject curMap = Manager.mapManager.getMap(player.gainMapId());
        if (null == curMap) {
            return;
        }

        boolean isCross = GameServer.getInstance().IsFightServer();

        Transport trans = curMap.getTransports().get(transportID);
        int res = isCanEnter(player, curMap, trans);

        if (res != MapDefine.CHANGE_MAP_RESULT_SUCCESS) {
            log.error(player + "进入地图失败 tranportId:" + transportID + " error=" + res);
            MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, res,  -1, -1);
            return;
        }

        if(isCross){
            //天墟战场传送检查
            UniverseWarData uwData = MapParam.getUniverseWarData(curMap);
            if(uwData!=null){
                int camp = MapParam.getUniverseWarData(curMap).getCampMap().get(player.playerCrossData.platSid);
                Map<Integer,Integer> tMap = uwData.getTransportMap().get(camp);
                if(tMap == null||!tMap.containsKey(transportID)){
                    return;
                }
            }
        }

        if (trans.isJump()) {
            onJumpFly(player, trans.getTargetID());
            return;
        }

        Cfg_Mapsetting_Bean mapCfg = CfgManager.getCfg_Mapsetting_Container().getValueByKey(trans.getTargetID());
        int mapId = mapCfg.getMap_id();
        //如果传送的目标是跨服
        if (mapCfg.getType() == MapDefine.CROSS_WORLD_MAP) {
            if (isCross) {
                if (ConnectFightManager.GetInstance().getMapIdInFight().containsKey(mapCfg.getMap_id())) {
                    moveToOtherWorldMap(player, trans.getTargetID(), trans.getTargetPos(), trans.getId());
                } else {
                    //如果要去的目标没在本跨服， 则离开此跨服
                    if (OnF2GPlayerOutCrossWorldMap(player, mapId, trans)) {
                        Manager.mapManager.manager().onCrossOutMap(player);
                        player.dealOffLine();
                    } else {
                        MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_CROSS_MAP_CHANGE,  -1, -1);
                    }
                }
                return;
            }

            //通知进入跨服野图
            if (ConnectFightManager.GetInstance().getMapIdInFight().containsKey(mapCfg.getMap_id())) {
                int fid = ConnectFightManager.GetInstance().getMapIdInFight().get(mapCfg.getMap_id());
                CrossFightMessage.roleAtt.Builder ratt = CrossFightMessage.roleAtt.newBuilder();
                ratt.setRoleId(player.getId());
                if (Manager.playerManager.managerExt().OnSynPlayerInfoToFight(fid, player, 0, 0, ratt.build(),
                        mapCfg.getMap_id(), 0, new ArrayList<>(), false)) {
                    return;
                }
            }
            //同步切换结果
            MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_CROSS_MAP_NOT_SERVER, -1, -1);
            return;
        } else {
            if (isCross) {
                if(trans.getTargetID() == curMap.getMapModelId()){
                    ResCurMapTransport(player, curMap, trans.getTargetPos(), 0, 0);
                    log.error(player.nameIdString() + "->>传送到-->>" + trans.getTargetPos());
                    return;
                }else if (OnF2GPlayerOutCrossWorldMap(player, mapId, trans)) {
                    Manager.mapManager.manager().onCrossOutMap(player);
                    player.dealOffLine();
                } else {
                    MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_CROSS_MAP_CHANGE,  -1, -1);
                }
                return;
            }
            if (trans.getTargetID() == curMap.getMapModelId()) {
                ResCurMapTransport(player, curMap, trans.getTargetPos(), 0, 0);
                log.error(player.nameIdString() + "->>传送到-->>" + trans.getTargetPos());
            } else {
                moveToOtherWorldMap(player, trans.getTargetID(), trans.getTargetPos(), trans.getId());
            }
        }
    }

    @Override
    public void onJumpFly(Player player, int transportID) {
        //传送目标为空
        Vector3 target = Manager.mapCfgManager.getJumpTransport().get(transportID);
        if (target == null) {
            MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_UN_FIND, -1, -1);
            return;
        }
        Position nowPosition = player.getCurGps().getPos();
        double distance = Utils.getDistance(target, nowPosition);
        if (distance <= 3) {
            log.error("玩家就在目标点不必跳跃传送！");
            return;
        }
        player.changeCurPos(target, true);

        //宠物同步跳跃过去
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet != null) {
            pet.changeCurPos(target, true);
        }

        ResJumpTransport.Builder jump = ResJumpTransport.newBuilder();
        jump.setTransId(transportID);
        MessageUtils.send_to_player(player, ResJumpTransport.MsgID.eMsgID_VALUE, jump.build().toByteArray());

        if (Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            for (Long passenger : passengers) {
                Player passengerPlayer = Manager.playerManager.getPlayerOnline(passenger);
                if (passengerPlayer != null) {
                    ResJumpTransport.Builder newJump = ResJumpTransport.newBuilder();
                    newJump.setTransId(transportID);
                    MessageUtils.send_to_player(passengerPlayer, ResJumpTransport.MsgID.eMsgID_VALUE, newJump.build().toByteArray());
                }
            }
        }
    }

    private boolean OnF2GPlayerOutCrossWorldMap(Player player, int toMapId, Transport trans) {
        F2GPlayerOutCrossWorldMap.Builder msg = F2GPlayerOutCrossWorldMap.newBuilder();
        msg.setFid(ServerConfig.getServerId());
        msg.setFromMapId(player.gainMapModelId());
        msg.setFx((int) player.gainCurPos().getX());
        msg.setFy((int) player.gainCurPos().getY());
        msg.setRoleId(player.getId());
        msg.setToMapId(toMapId);
        msg.setTransId(trans.getId());
        msg.setTox((int) trans.getTargetPos().getX());
        msg.setToy((int) trans.getTargetPos().getY());
        return FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GPlayerOutCrossWorldMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void moveToOtherWorldMap(Player player, int mapid, Position pos, int transportId) {
        //过地图自动从飞行状态下来
        if (player.getHorse() != null && player.getHorse().getRideState() == HorseRideStateEnum.Fly) {
            if (BehaviorManager.HasBehavior(player, BehaviorType.Fly)) {
                BehaviorManager.CancelBehaviorByType(player, BehaviorType.Fly);
            }
            Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapid);
            if (bean.getCanRiding() > 0) {
                player.getHorse().setRideState(HorseRideStateEnum.Ride);
            } else {
                player.getHorse().setRideState(HorseRideStateEnum.UnRide);
            }
        }

        Manager.mapManager.changeMap(player, mapid, pos, -1, false);
        Manager.cooldownManager.addCooldown(player, CooldownTypes.ChangeMap, null, GlobalType.ChangeMapCD);
        if (Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            for (Long passenger : passengers) {
                Player passengerPlayer = Manager.playerManager.getPlayerOnline(passenger);
                if (passengerPlayer != null) {
                    Manager.mapManager.changeMap(passengerPlayer, mapid, pos, -1, false);
                    Manager.cooldownManager.addCooldown(passengerPlayer, CooldownTypes.ChangeMap, null, GlobalType.ChangeMapCD);
                }
            }
        }
    }

    /**
     * 能否进入地图，条件不满足会给出提示
     *
     * @param player
     * @param mapID
     * @return
     */
    @Override
    public boolean canEnterMap(Player player, int mapID) {
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapID);
        if (bean == null) {
            return false;
        }

        // 等级
        if (!MapUtils.isLevelCanEnter(player, mapID)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_CHANGEMAP_FAILED_LEVEL);
            return false;
        }

        if (bean.getNeedState() > player.getStateVip().getLv()) {
            Cfg_State_power_Bean stateBean = CfgManager.getCfg_State_power_Container().getValueByKey(bean.getNeedState());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.VipNotEnough, ServerStr.getChatTableName(stateBean.getName()));
            return false;
        }

        if (player.playerCrossData.crossState != CrossState.PCS_LOCAL) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CROSSFIGHT_ISWAIT);//("您已经在副本中。"));
            return false;
        }

        // 如果正在切换地图
        return !EntityState.ChangeMap.compare(player.getState());
    }

    //传送管理
    @Override
    public void OnReqTransportControl(Player player, MapMessage.ReqTransportControl msg) {
        log.info("客户端请求切换场景:" + player.toString() + "ReqTransportControl:" + msg.getMapID());
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(msg.getMapID());
        if (bean == null) {
            return;
        }

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.TransportCD, null)) {
            long remainTime = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.TransportCD, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, String.valueOf((remainTime + 999) / 1000));
            return;
        }

        if (!canEnterMap(player, msg.getMapID()))
            return;

        if (!player.getBeEnemys().isEmpty()) {
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_BATTLE, msg.getType(), msg.getParam());
            return;
        }

        //如果玩家死亡
        if (player.isDie()) {
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_PLAYER_DIE, msg.getType(), msg.getParam());
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        //在副本中不能传送
        if (map.getZoneModelId() > 0) {
            MapUtils.sendChangeMap(player, 0, 0, Position.ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_LOCATION_COPY_MAP, msg.getType(), msg.getParam());
            return;
        }
        onTransport2world(player, bean, msg);
        //添加CD
        Manager.cooldownManager.addCooldown(player, CooldownTypes.TransportCD, null, 2000);
    }



    //世界地图传送
    private void onTransport2world(Player player, Cfg_Mapsetting_Bean bean, MapMessage.ReqTransportControl msg) {
        Position pos = null;
        if (msg.getType() == 2) {
            //小飞鞋
            int mapID = msg.getMapID();
            pos = new Position(msg.getX(), msg.getY());
            if (player.gainMapModelId() == mapID) {
                float distance = Utils.getDistance(player.gainCurPos(), pos);
                if (distance < Global.FlyShoeMinDistance) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.TransportDisLess30);
                    MapUtils.sendChangeMap(player, 0, 0, player.gainCurPos(), MapDefine.TRANSPORT_ERROR_LESS30, -1, -1);//增加一个编码错误，用于确定玩家传送是小于规则距离的
                    return;
                }
            }

            //检测传送资格
            if (!Manager.vipManager.power().isCanFreeFly(player)) {
                int needItemId = Global.FlyShoeID;
                if (!Manager.backpackManager.manager().onRemoveItem(player, needItemId, 1, ItemChangeReason.MiniMapTransDec, IDConfigUtil.getLogId())) {
                    Manager.backpackManager.manager().sendItemNotEnough(player, needItemId);
//                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnoughShowName, Manager.backpackManager.manager().getName(needItemId));
                    return;
                }else{//没有飞鞋，且不满足vip特权
                    if(!player.getVipPearl().canFree()){
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_Vip_Power_No_Ues_Notice);
                    }
                }
            }
        }

        Manager.mapManager.changeMap(player, bean.getMap_id(), pos, msg.getType(), false);
        if (Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            for (Long passenger : passengers) {
                Player passengerPlayer = Manager.playerManager.getPlayerOnline(passenger);
                if (passengerPlayer != null) {
                    Manager.mapManager.changeMap(passengerPlayer, bean.getMap_id(), pos, msg.getType(), false);
                }
            }
        }
    }

    //本地图传送
    @Override
    public void ResCurMapTransport(Player player, MapObject map, Position pos, int type, long param) {
        if (pos == null) {
            return;
        }

        if (!Utils.isCanMove(map, pos)) {
            pos = map.getBrithPos();
        }

        //取消玩家的所有行为
        BehaviorManager.CancelAllBehavior(player);
        player.setInBattle(false);
        player.clearHatred();
        player.setCurAttackTargetId(0);

        player.changeCurPos(pos, true);
        MapUtils.sendChangeMap(player, map.getMapModelId(), player.gainLine(), pos, MapDefine.CHANGE_MAP_RESULT_SUCCESS, type, param);
    }
}
