package com.game.publicserver.timer;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.publicserver.structs.ConnectPublicServer;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.timer.TimerEvent;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PConnectHeart;
import io.netty.channel.ChannelHandlerContext;


/**
 * @author lw
 */
public class PublicHeartTimer extends TimerEvent {

    public PublicHeartTimer() {
        super(-1, 0,30 * 1000); //30秒一次
    }

    @Override
    public void action() {
        ConnectPublicServer connectPublicServer = Manager.publicServerManager.getPublicServer();
        if (connectPublicServer == null) {
            return;
        }

        ChannelHandlerContext iosession = Manager.publicServerManager.getPublicSession();
        if (iosession == null) {
            Manager.publicServerManager.reconnect();
            return;
        }

        if (iosession.isRemoved()) {
            Manager.publicServerManager.reconnect();
            return;
        }

        //发送心跳
        G2PConnectHeart.Builder msg = G2PConnectHeart.newBuilder();
        msg.setPlat(ServerConfig.getServerPlatform());
        msg.setServerId(ServerConfig.getServerId());

        if (GameServer.getInstance().IsFightServer()) {
            //拿出所有副本
            int roleSize = 0;
            for (MapObject mapObject : Manager.mapManager.getMaps().values()) {
                roleSize += mapObject.getPlayers().size();
            }
            msg.setRoleSize(roleSize);
        }
        MessageUtils.send_to_public(G2PConnectHeart.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        long now = System.currentTimeMillis();
        long offset = now - Manager.publicServerManager.getHeartTime();

        if (offset > 60 * 1000) {
            Manager.publicServerManager.reconnect();
        }
    }

}
