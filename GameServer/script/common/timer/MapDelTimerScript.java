package common.timer;

import com.game.copymap.structs.FightRoomState;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDelTimerScript implements IScript {

    protected Logger log = LogManager.getLogger(MapDelTimerScript.class);
    //基础设为2分钟
    private static final int DES_TIME = 1000 * 60 * 2;

    @Override
    public int getId() {
        return ScriptEnum.MapDelBaseScript;
    }

    @Override
    public Object call(Object... args) {
        long now = TimeUtils.Time();
        for (MapObject mapObject : Manager.mapManager.getMaps().values()) {
            if (mapObject.getType() == MapDefine.WORLD_MAP) {
                worldMapCheck(mapObject, now);
            } else {
                copyMapCheck(mapObject, now);
            }
        }

        return null;
    }

    private void worldMapCheck(MapObject mapObject, long now) {
        if (mapObject.getLineId() == 1) {
            return;
        }

        if (mapObject.getLastHasPlayerTime() == 0) {
            mapObject.setLastHasPlayerTime(now);
        }

        if (!mapObject.getPlayers().isEmpty()) {
            mapObject.setLastHasPlayerTime(now);
        }

        if (now - mapObject.getLastHasPlayerTime() < DES_TIME) {
            return;
        }

        log.info("世界地图销毁:" + mapObject.getId() + ";mapModelId:" + mapObject.getMapModelId() + ";line:" + mapObject.getLineId());
        Manager.mapManager.deleteMap(mapObject);
    }

    private void copyMapCheck(MapObject mapObject, long now) {
        if (mapObject.getLastHasPlayerTime() == 0) {
            mapObject.setLastHasPlayerTime(now);
            return;
        }

        if (mapObject.getDelTime() != 0 && mapObject.getDelTime() < now) {
            log.info("副本存在时间到期销毁:" + mapObject.getId() + ";ZoneModelId:" + mapObject.getZoneModelId() + ";deleteTime:" + mapObject.getDelTime() + ";monsterSize:" + mapObject.getMonsters().size());
            operateDelete(mapObject);
            return;
        }

        if (!mapObject.getPlayers().isEmpty()) {
            mapObject.setLastHasPlayerTime(now);
            return;
        }

        if (mapObject.isAutoRemove() && (now - mapObject.getLastHasPlayerTime()) >= DES_TIME) {
            log.info("副本无人地图销毁:" + mapObject.getId() + ";ZoneModelId:" + mapObject.getZoneModelId() + ";monsterSize:" + mapObject.getMonsters().size());
            operateDelete(mapObject);
        }
    }


    private void operateDelete(MapObject mapObject) {
        if (GameServer.getInstance().IsFightServer()) {
            operateDeleteFightContext(mapObject);
            Manager.mapManager.deleteMap(mapObject);
        } else {
            operateDeleteGameContext(mapObject);
            Manager.mapManager.deleteMap(mapObject);
        }

    }

    private void operateDeleteGameContext(MapObject mapObject) {
        for (Player player : mapObject.getPlayers().values()) {
            if (player.isDie()) {
                Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, ReliveType.Gm, false, player.gainCurPos());
            }
            Manager.copyMapManager.outZone(player);
        }
    }

    private void operateDeleteFightContext(MapObject mapObject) {
        HashMap<ChannelHandlerContext, List<Long>> ppcache = new HashMap<>();
        for (Player player : mapObject.getPlayers().values()) {
            ChannelHandlerContext session = player.getIosession();
            if (session == null) {
                player.dealOffLine();
                log.error(player.getName() + "(" + player.getId() + ") 在跨服战与原服断开了连接了！");
            } else {
                List<Long> sp = ppcache.computeIfAbsent(session, k -> new ArrayList<>());
                sp.add(player.getId());
                Manager.buffManager.deal().onDie(player);
                player.dealOffLine();
                player.setIosession(null);
                player.setIsOnline((byte) 0);
            }
        }
        //发送给公共服地图销毁
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTEND);
        //----
        CrossServerMessage.F2GCloneClose.Builder msg = CrossServerMessage.F2GCloneClose.newBuilder();
        msg.setFightId(mapObject.getId());
        msg.setModelId(mapObject.getZoneModelId());
        for (java.util.Map.Entry<ChannelHandlerContext, List<Long>> en : ppcache.entrySet()) {
            if (msg.getRoleIdsCount() > 0) {
                msg.clearRoleIds();
            }
            msg.addAllRoleIds(en.getValue());
            if (FightClientManager.GetInstance().send_to_game(en.getKey(), CrossServerMessage.F2GCloneClose.MsgID.eMsgID_VALUE, msg.build().toByteArray())) {
                log.error("游戏服:" + en.getKey() + "的离开消息发送成功！");
            } else {
                log.error("游戏服:" + en.getKey() + "的离开消息发送失败了！");
            }
        }
    }
}
