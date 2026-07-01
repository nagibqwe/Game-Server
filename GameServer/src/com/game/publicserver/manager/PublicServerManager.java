package com.game.publicserver.manager;

import com.game.crossrank.timer.CrossRankTimer;
import com.game.player.structs.SessionAttribute;
import com.game.publicserver.structs.ConnectPublicServer;
import com.game.publicserver.timer.PublicHeartTimer;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import game.core.util.TimeUtils;
import game.message.serverMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static game.core.util.SessionUtils.SEND_BUF;

public class PublicServerManager {
    private final static Logger logger = LogManager.getLogger(PublicServerManager.class);

    private ConnectPublicServer publicServer;

    private ChannelHandlerContext publicSession;

    private long heartTime = 0;

    PublicHeartTimer publicHeartTimer;

    public ConnectPublicServer getPublicServer() {
        return publicServer;
    }

    public void setPublicServer(ConnectPublicServer publicServer) {
        this.publicServer = publicServer;
    }

    public ChannelHandlerContext getPublicSession() {
        return publicSession;
    }

    public void setPublicSession(ChannelHandlerContext publicSession) {
        this.publicSession = publicSession;
    }

    public long getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }

    public void init() {
        if (StringUtils.isBlank(ServerConfig.getPublicIp())) {
            return;
        }
        publicServer = new ConnectPublicServer(ServerConfig.getPublicIp(), ServerConfig.getPublicPort(), ServerEnum.PUBLIC_SERVER);
    }

    public void start() {
        if (publicServer != null) {
            new Thread(publicServer, "公共服连接").start();
        }
    }

    public void stop() {
        if (publicServer != null) {
            publicHeartTimer = null;
            publicServer.stop();
            publicServer = null;
        }
    }

    public void reconnect() {
        logger.info("current thread name :" + Thread.currentThread().getName() + " thread id:" + Thread.currentThread().getId());
        GameServer.getInstance().getMainThread().removeTimerEvent(publicHeartTimer);
        publicHeartTimer = null;
        publicServer.connect();
    }

    public void register(ChannelHandlerContext session) {
        session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).set(publicServer.id);
        session.channel().attr(SessionAttribute.CONNECT_SERVER_IP).set(publicServer.publicIp);
        session.channel().attr(SessionAttribute.CONNECT_SERVER_PORT).set(publicServer.publicPort);
        logger.info(" 注册连接public服成功 id=:" + publicServer.id);
        publicSession = session;
        registerToPulbic();
        heartTime = TimeUtils.Time() + 30 * 1000;//延长处理心跳的问题
        if (publicHeartTimer == null) {
            publicHeartTimer = new PublicHeartTimer();
            GameServer.getInstance().getMainThread().addTimerEvent(publicHeartTimer);
        }
        if(!GameServer.getInstance().IsFightServer())
            GameServer.getInstance().getMainThread().addTimerEvent(new CrossRankTimer());
    }

    public void sendToPublic(SMessage message) {
        if (publicSession != null) {
            //session.write(message);//写消息
            ByteBuf buf = null;
            try {
                int len = (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE + message.getData().length;
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(0);
                buf.writeInt(message.getId());
                buf.writeLong(message.getSender());
                buf.writeBytes(message.getData());
                synchronized (publicSession) {
                    ByteBuf out = publicSession.channel().attr(SEND_BUF).get();
                    if (out == null) {
                        publicSession.channel().attr(SEND_BUF).set(buf);
                    } else {
                        out.writeBytes(buf);
                        buf.release();
                    }
                }
            } catch (Exception e) {
                logger.error("world处理消息协议时出错了，！", e);
            }
        } else {
            logger.error("发送公共服消息时， 连接已经不存在了！， ID= " + message.getId());
            GameServer.getInstance().setErrorLog("game send to public", " 连接已经断开了，发不了内容！");
        }
    }

    private void registerToPulbic() {
        serverMessage.G2PReqRegister.Builder msg = serverMessage.G2PReqRegister.newBuilder();
        serverMessage.gameServerInfo.Builder sinfo = serverMessage.gameServerInfo.newBuilder();
        sinfo.setPlatformMark(ServerConfig.getServerPlatform());
        sinfo.setServerIP(ServerConfig.getGameServerIp());
        sinfo.setServerId(ServerConfig.getServerId());
        sinfo.setServerPort(ServerConfig.getServerPort());
        sinfo.setServerType(GameServer.getInstance().IsFightServer() ? ServerEnum.FIGHT_SERVER_LISTEN : ServerConfig.GetServerType());
        sinfo.setVersion(GameServer.version);
        sinfo.setServerOpentime(ServerConfig.getServerOpenTime());
        sinfo.setServerWorldlv(GlobalType.getWorldLevel());
        msg.setSinfo(sinfo);
        msg.addAllCombinedIds(ServerConfig.getServerIdList());
        MessageUtils.send_to_public(serverMessage.G2PReqRegister.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private enum Singleton {
        INSTANCE;
        PublicServerManager manager;

        Singleton() {
            this.manager = new PublicServerManager();
        }
        PublicServerManager getProcessor() {
            return manager;
        }
    }

    public static PublicServerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
