package common.register;

import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.EquipPartBaseType;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.register.script.ILoadScript;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage;
import game.message.CrossFightMessage.G2FOnEnterMapAgain;
import game.message.CrossFightMessage.G2PCheckCrossInfo;
import game.message.MapMessage;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author admin
 */
public class LoadScript implements IScript, ILoadScript {

    private static final Logger logger = LogManager.getLogger(LoadScript.class);
    private static final Logger log = LogManager.getLogger("com.game.register.manager.RegisterManager");

    @Override
    public int getId() {
        return ScriptEnum.LoadScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void EnterGameMap(Player player) {
        try {
            logger.info("进入地图流程开始 player={}", player);
            if ((Manager.countManager.getVariant(player, VariantType.Today_First_Login_Level) < 1)) {
                Manager.countManager.addVariant(player, VariantType.Today_First_Login_Level, player.getLevel());
            }

            //如果还有跨服标志，检测能否进入跨服
            if (player.playerCrossData.toFightId > 0 && player.playerCrossData.toZoneModelId != 0) {
                player.playerCrossData.setToFightServer(false);
                player.playerCrossData.isReqFight = false;
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
                logger.info("玩家地图信息初始化完毕" + player + ", 并进入跨服");
                checkCrossInfo(player);
                return;
            } else if (player.playerCrossData.toFightId != 0) {
                player.playerCrossData.toFightId = 0;
                player.playerCrossData.setToFightServer(false);
                player.playerCrossData.isReqFight = false;
                logger.info("把玩家还原到本服" + player + ", 并进入跨服   " + player.playerCrossData.toFightId);

            }
            //在没有把玩家拉到战斗服的情况下修正进入标志
            if (player.playerCrossData.isReqFight) {
                long now = TimeUtils.Time();
                if (now - player.playerCrossData.reqFightTime > 150000) {
                    player.playerCrossData.isReqFight = false;
                }
            }

            if (player.playerCrossData.crossState > CrossState.PCS_LOCAL) {
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            }
            int mapId = player.gainMapModelId();

            //如果玩家唉在跨服休息室地图，服务器重启了
            if (mapId == 500) {
                MapObject unknowMap = Manager.mapManager.getMap(player.gainMapId());
                logger.info("玩家地图信息初始化完毕, 再跨服休息室" + player);
                //把玩家从所的跨服战斗服进行踢出
                if (unknowMap != null)
                    Manager.mapManager.manager().onQuitMap(unknowMap, player, false);
                //Manager.playerManager.managerExt().onCrossPlayerOut(player);
                Manager.mapManager.changeMap(player, Manager.playerManager.getBornMapID(), null, -1, true);
                return;
            }
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map != null && map.getType() != MapDefine.WORLD_MAP && !map.isStop()) {
                Manager.mapManager.changeMap(player, map.getId(), player.gainCurPos(), true);
                return;
            }
            Manager.mapManager.changeMap(player, player.gainMapModelId(), player.gainCurPos(), -1, true);
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
    }


    //检测跨服战场结束否
    public void checkCrossInfo(Player player) {
        G2PCheckCrossInfo.Builder msg = G2PCheckCrossInfo.newBuilder();
        msg.setRoleId(player.getId());
        msg.setRoomId(player.playerCrossData.toFightId);
        MessageUtils.send_to_public(G2PCheckCrossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 处理客户端的主界面引导ID
     *
     * @param player
     * @param lastId
     */
    @Override
    public void OnReqUpdateMainUIGuideID(Player player, int lastId) {
        if (player == null) {
            return;
        }
        if (lastId < 1) {
            logger.error(" 更新主界面引导ID时出错了！");
            return;
        }

        player.setMainGuide(lastId);
        sendMainGuide(player);
    }

    @Override
    public void OnReqMainUIGuideID(Player player) {
        if (player != null) {
            sendMainGuide(player);
        } else {
            logger.error("OnReqMainUIGuideID 调用错误，传送了错误的参数！");
        }
    }

    private void sendMainGuide(Player player) {
        PlayerMessage.ResMainUIGuideID.Builder msg = PlayerMessage.ResMainUIGuideID.newBuilder();
        msg.setGid(player.getMainGuide());
        MessageUtils.send_to_player(player, PlayerMessage.ResMainUIGuideID.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //客户端加载完成
    @Override
    public void OnReqLoadFinish(Player player) {
        //进入地图
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (EntityState.ChangeMap.compare(player.getState())) {
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ChangeMap);
            return;
        }
        //重连的情况下是支持重新加载的
        if (EntityState.ReConnect.compare(player.getState())) {
            //最后进入地图
            if (player.playerCrossData.toFightId > 0) {
                //发送进入地图到跨服
                G2FOnEnterMapAgain.Builder entercross = G2FOnEnterMapAgain.newBuilder();
                entercross.setRoleId(player.getId());
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FOnEnterMapAgain.MsgID.eMsgID_VALUE, entercross.build().toByteArray());
                player.playerCrossData.setToFightServer(true);
                player.playerCrossData.isReqFight = false;
                return;
            }
            //修改位置， 使用在线标志更新
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (pwi != null) {
                pwi.setLastOffTime(0);//玩家在线哦
            } else {
                log.error(player + "登录时没能生成离线数据", new NullPointerException());
            }
            log.error(player.nameIdString() + " 玩家回复了重连接成功了！");
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ReConnect);
            return;
        }

        //登录加载完成
        if (EntityState.LoginGame.compare(player.getState())) {

            //修改位置， 使用在线标志更新
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (pwi != null) {
                pwi.setLastOffTime(0);//玩家在线哦
            }
            Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);
            //数据检查设置
            checkPlayerData(player);
            //同步玩家所有数据
            Manager.playerManager.manager().OnSendPlayerAllInfo(player, false);
            //最后进入地图
            if (player.playerCrossData.toFightId > 0) {
                //发送进入地图到跨服
                G2FOnEnterMapAgain.Builder entercross = G2FOnEnterMapAgain.newBuilder();
                entercross.setRoleId(player.getId());
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FOnEnterMapAgain.MsgID.eMsgID_VALUE, entercross.build().toByteArray());
                player.playerCrossData.setToFightServer(true);
                player.playerCrossData.isReqFight = false;
            } else {
                //清理路径
                player.clearRoads();
                Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            }

            player.removeSate(EntityState.LoginGame);
            logger.info(player + "登录完成！mapId=" + player.gainMapId());
            try {
                Manager.redPacketManager.getScript().playerLogin(player);
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }

        //如果不是战斗服则不管， 如果是战斗服， 检查玩家是否有分配地图， 如果没有， 则另处理
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }

        if (map != null) {
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ChangeMap);
            MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
            msg.setId(player.getId());
            msg.setTarget(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        } else {
            CrossFightMessage.F2GEnterCloneMapRes.Builder msg = CrossFightMessage.F2GEnterCloneMapRes.newBuilder();
            msg.setRoleId(player.getId());
            msg.setParam(-1);
            msg.setLineId(-1);
            msg.setX(-1);
            msg.setY(-1);
            FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossFightMessage.F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

    }

    /**
     * 检查数据 并重新设置
     * @param player
     */
    private void checkPlayerData(Player player) {
        //装备部位检查
        List<EquipPart> parts = player.getEquipParts();
        //增加部位装备
        if(parts.size() < EquipDefine.EquipPart_Num){
            for(int i= parts.size();i<EquipDefine.EquipPart_Num;i++){
                EquipPart part = new EquipPart();
                part.setCurrentExp(0);
                part.setType(i);
                part.setLevel(0);
                parts.add(part);
            }
        }

        //初始化宝石孔位
        Manager.gemManager.deal().initGemInfo(player);
    }

    //断线重连
    @Override
    public void reconnect(Player player) {
        player.resetState();
//		Manager.mapManager.allocateMap(player);

        Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);
        Manager.playerManager.manager().OnSendPlayerAllInfo(player, true);
        player.removeSate(EntityState.ExitGame);
        player.addState(EntityState.Stand);
        player.addState(EntityState.ReConnect);

        if (player.playerCrossData.toFightId > 0) {
            player.playerCrossData.setToFightServer(true);
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_FIGHT;
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map != null && map.getType() != MapDefine.WORLD_MAP && !map.isStop()) {
            Manager.mapManager.changeMap(player, map.getId(), player.gainCurPos(), true);
            return;
        }
        Manager.mapManager.changeMap(player, player.gainMapModelId(), player.gainCurPos(), -1, true);

        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if (pwi != null) {
            pwi.setLastOffTime(0);//玩家在线哦
        }
    }
}
