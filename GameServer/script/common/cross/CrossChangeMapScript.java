/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.cross;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.copymap.log.CopyMapEnterLog;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.CopyMapType;
import com.game.copymap.structs.FightRoomState;
import com.game.crossfight.log.CrossCloneEnterLog;
import com.game.crossserver.scripts.ICrossChangeMapScript;
import com.game.fightserver.manager.FightClientManager;
import com.game.horse.structs.Horse;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.ICrossCloneScript;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.*;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.message.CrossFightMessage.F2GEnterCloneMapRes;
import game.message.CrossFightMessage.G2FEnterCloneMap;
import game.message.CrossFightMessage.G2FOnEnterMapAgain;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author admin
 */
public class CrossChangeMapScript implements IScript, ICrossChangeMapScript {

    private final static Logger LOG = LogManager.getLogger(CrossChangeMapScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CrossChangeMapBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 进入跨服副本消息处理
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnG2FEnterCloneMap(ChannelHandlerContext context, G2FEnterCloneMap messInfo) {
        F2GEnterCloneMapRes.Builder msg = F2GEnterCloneMapRes.newBuilder();
        msg.setRoleId(messInfo.getRoleId());
        msg.setParam(-1);
        msg.setLineId(-1);
        msg.setX(-1);
        msg.setY(-1);
        Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
        if (player == null) {
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            LOG.error("messInfo.getRoleId()={} roomId={}进入跨服副本失败,玩家数据消失", messInfo.getRoleId(), messInfo.getRoomId());
            return;
        }
        //获取副本信息
        MapObject mapObject = Manager.mapManager.getMap(messInfo.getRoomId());
        if (mapObject == null) {
            if (!messInfo.getOnlyJoin()) {
                mapObject = Manager.mapManager.createCopyMap(messInfo.getCloneId(), messInfo.getZoneLevel(), MapManager.CopyMapOwnerSystemId, messInfo.getRoomId(), messInfo.getMapSetListList());
                Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHT_WAIT);
            }
        }
        if (mapObject == null) {
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            Manager.crossServerManager.getCrossServer().SendFightStateToPublic(messInfo.getRoomId(), FightRoomState.FIGHTEND);
            LOG.error("messInfo.getRoleId()={} roomId={}进入跨服副本失败, 副本已销毁", messInfo.getRoleId(), messInfo.getRoomId());
            return;
        }
        //游戏服缓存
        Integer state = FightClientManager.GetInstance().getRoomStateCache().get(messInfo.getRoomId());
        if (state != null && state >= FightRoomState.FIGHTEND) {
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            LOG.error("进入跨服副本失败 房间战斗已经结束了:" + mapObject + "player:" + player);
            return;
        }

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(messInfo.getCloneId());
        //默认和平模式
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, false);
        Horse horse = player.getHorse();
        if (horse != null) {
            horse.setRideState(HorseRideStateEnum.UnRide);
        }
        player.addState(EntityState.ChangeMap);

        CrossCloneEnterLog cclog = new CrossCloneEnterLog();
        cclog.setCampNo(player.playerCrossData.fightCampNo);
        cclog.setFightId(messInfo.getRoomId());
        cclog.setCloneId(messInfo.getCloneId());
        cclog.setPlatSid(player.playerCrossData.platSid);
        cclog.setPlayer(player);
        cclog.setCloneName(bean.getDuplicate_name());
        cclog.setLevel(player.getLevel());
        LogService.getInstance().execute(cclog);

        //记录进入日志， 以满足后台能查询
        CopyMapEnterLog copyMapEnterLog = new CopyMapEnterLog();
        copyMapEnterLog.setAuto(0);
        copyMapEnterLog.setLevel(player.getLevel());
        copyMapEnterLog.setCloneId(messInfo.getCloneId());
        copyMapEnterLog.setPlayer(player);
        LogService.getInstance().execute(copyMapEnterLog);
        LOG.info(player.getName() + "(" + player.getId() + ")(" + player.playerCrossData.platSid + ") 加入了战斗服 fightid:" + messInfo.getRoomId());
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map != null) {
            //断线重连的
            msg.setRoleId(messInfo.getRoleId());
            msg.setParam(map.getMapModelId());
            msg.setLineId(map.getLineId());
            msg.setX(player.gainCurPos().getX());
            msg.setY(player.gainCurPos().getY());
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        LOG.info(player.getName() + "(" + player.getId() + ")(" + player.playerCrossData.platSid + ") 加入了第一次来战斗服 fightid:" + messInfo.getRoomId());
        //第一次登录的
        player.changeMapId(mapObject.getId());
        player.changeMapModelId(mapObject.getMapModelId());
        player.changeLine(mapObject.getLineId());

        Position brith = mapObject.getBrithPos();

        if (messInfo.getCross().getX() > 0 && messInfo.getCross().getY() > 0) {
            brith = new Position(messInfo.getCross().getX(), messInfo.getCross().getY());
        }
        IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
        if (is instanceof ICopyReliveScript) {
            ICopyReliveScript ss = (ICopyReliveScript) is;
            Position position = ss.doCreateRelivePosition(mapObject, player);
            if (position != null) {
                brith = position;
            }
        }
        player.changeCurPos(brith);

        //如果实在是没有坐标
        if (player.gainCurPos() == null) {
            LOG.error(player + " ,进地图 map =" + mapObject.getMapModelId() + " 没有出生点");
            player.changeCurPos(new Position(0, 0));
        }

        //通知玩家的血量
        player.onHpChange(null);
        player.setCamp(player.playerCrossData.fightCampNo);

        //设置副本的玩家ID及副本ID
        player.playerCrossData.toFightId = mapObject.getId();
        player.playerCrossData.toZoneModelId = mapObject.getZoneModelId();

        if (mapObject.getSetting().getIsscript() > 0) {
            IMapBaseScript base = Manager.mapManager.base(mapObject.getSetting().getIsscript());
            if (base instanceof ICrossCloneScript) {
                ((ICrossCloneScript) base).enterCross(player, mapObject, messInfo.getMapSetListList());
            }
        }

        msg.setRoleId(messInfo.getRoleId());
        msg.setParam(player.gainMapModelId());
        msg.setLineId(player.gainLine());
        msg.setX(player.gainCurPos().getX());
        msg.setY(player.gainCurPos().getY());

        FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        LOG.info(player.getName() + "(" + player.getId() + ")(" + player.playerCrossData.platSid + ") 通知游戏进入了战斗服");

        if (mapObject.getSetting().getIsscript() != ScriptEnum.BravePeakCrossActivityScript) {
            Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 0, 1, 0, false);
        }
        Manager.biManager.getScript().biScene(player, mapObject.getMapModelId(), mapObject.getName(), 3, 0);
    }

    /**
     * 进入跨服返回
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnF2GEnterCloneMapRes(ChannelHandlerContext context, F2GEnterCloneMapRes messInfo) {
        Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (player == null) {
            LOG.error("玩家已经任性的下线了 roleID" + messInfo.getRoleId());
            return;
        }
        if (messInfo.getParam() <= 0) {
            LOG.error("进入跨服失败" + player);
            player.playerCrossData.setToFightServer(false);
            player.playerCrossData.toFightId = 0;
            player.playerCrossData.toFightSid = 0;
            player.playerCrossData.toZoneModelId = 0;
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_LOCAL;
//            manager.scriptManager.callMethod(ScriptEnum.CrossServerScript, "quitUnknowMap", player);
            Manager.crossServerManager.getCrossServer().OnQuitUnknowMap(player, -1);
            return;
        }
        player.playerCrossData.isReqFight = false;
        if (EntityState.LoginGame.compare(player.getState())) {
            //断线重连的
            MapUtils.sendPlayerLoadingMapID(player, messInfo.getParam(), messInfo.getLineId(), player.getId(), player.gainCurPos());
            player.playerCrossData.toFightPos = new Position(messInfo.getX(), messInfo.getY());
            LOG.error("进入跨服使用登录 roleID" + messInfo.getRoleId());
        } else {
            //进入跨服休息室
            if (player.gainMapModelId() != 500) {
                Manager.mapManager.changeSpaceMap(player);
                LOG.info(player.nameIdString() + " 玩家进入跨服休息室了！");
            }
            player.playerCrossData.setToFightServer(true);
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_FIGHT;
            MapUtils.sendChangeMap(player, messInfo.getParam(), messInfo.getLineId(), new Position(messInfo.getX(), messInfo.getY()), MapDefine.CHANGE_MAP_RESULT_SUCCESS, -1, -1);
            LOG.info("进入跨服后使用ChangeMap roleID={} x={}y={}", messInfo.getRoleId(), messInfo.getX(), messInfo.getY());
        }

    }

    //断线重连同步周围数据
    @Override
    public void OnEnterMapAgain(ChannelHandlerContext context, G2FOnEnterMapAgain messInfo) {
        Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
        if (player == null) {
            F2GEnterCloneMapRes.Builder msg = F2GEnterCloneMapRes.newBuilder();
            msg.setRoleId(messInfo.getRoleId());
            msg.setParam(-1);
            msg.setLineId(-1);
            msg.setX(-1);
            msg.setY(-1);
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        //战斗服已经不存在了
        if (map == null) {
            F2GEnterCloneMapRes.Builder msg = F2GEnterCloneMapRes.newBuilder();
            msg.setRoleId(messInfo.getRoleId());
            msg.setParam(-1);
            msg.setLineId(-1);
            msg.setX(-1);
            msg.setY(-1);
            FightClientManager.GetInstance().send_to_game(context, F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }

//        HashMap<String, Object> args = new HashMap<>();
//        args.put("player", player);
//        args.put("map", map);
//        args.put("pos", player.getCurPos());
//        //进入新地图
//        ScriptManager.getInstance().call(ScriptEnum.Enter_Map, args);
        Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
        player.removeSate(EntityState.ChangeMap);

    }

}
