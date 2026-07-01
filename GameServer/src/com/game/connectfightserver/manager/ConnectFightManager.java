package com.game.connectfightserver.manager;

import com.game.connectfightserver.struct.ConnectFightServer;
import com.game.player.structs.SessionAttribute;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import game.core.message.RMessage;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import static game.core.util.SessionUtils.SEND_BUF;
import game.core.util.ZLibUtils;
import game.message.serverMessage.G2FReqRegister;
import game.message.serverMessage.gameServerInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接战斗服管理器
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ConnectFightManager {

    private static final Logger log = LogManager.getLogger(ConnectFightManager.class);

    private enum Singleton {
        INSTANCE;
        ConnectFightManager processor;

        Singleton() {
            this.processor = new ConnectFightManager();
        }

        ConnectFightManager getProcessor() {
            return processor;
        }
    }

    public static ConnectFightManager GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //战斗服连接集合
    private final ConcurrentHashMap<Integer, ConnectFightServer> conList = new ConcurrentHashMap<>();

    //连接战斗服队列
    private ConcurrentHashMap<Integer, ChannelHandlerContext> fightConnects = new ConcurrentHashMap<>();

    //注册跨服野图的对应的战斗服
    private final ConcurrentHashMap<Integer, Integer> mapIdInFight = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Integer> getMapIdInFight() {
        return mapIdInFight;
    }

    public ConcurrentHashMap<Integer, ConnectFightServer> getConList() {
        return conList;
    }

    public ConcurrentHashMap<Integer, ChannelHandlerContext> getFightConnects() {
        return fightConnects;
    }

    public void setFightConnects(ConcurrentHashMap<Integer, ChannelHandlerContext> fightConnects) {
        this.fightConnects = fightConnects;
    }

    public void RegisterMapids(int fightid, List<Integer> mapids) {
        Iterator<Entry<Integer, Integer>> iter = mapIdInFight.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, Integer> en = iter.next();
            if (en.getValue() == fightid) {
                if (!mapids.contains(en.getKey())) {
                    log.error("战斗服ID：" + fightid + ",已经不包含当前地图ID：" + en.getKey());
                    iter.remove();
                }
            }
        }

        if (mapids.size() < 1) {
            return;
        }

        for (int mapId : mapids) {
            mapIdInFight.put(mapId, fightid);
            log.error("战斗服ID：" + fightid + ",注册当前地图ID：" + mapId);
        }
    }

    //注册的游戏服
    public void Register(ChannelHandlerContext session) {
//        int serverId = session.attr(SessionAttribute.CONNECT_SERVER_ID).get();
//        fightConnects.put(serverId, session);
        int serverId = 0;
        if (session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get() != null) {
            serverId = session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();
        }

        if (serverId != 0) {
            fightConnects.put(serverId, session);
        }
        gameServerInfo.Builder sinfo = gameServerInfo.newBuilder();
        sinfo.setPlatformMark(ServerConfig.getServerPlatform());
        sinfo.setServerIP(ServerConfig.getGameServerIp());
        sinfo.setServerId(ServerConfig.getServerId());
        sinfo.setServerPort(ServerConfig.getServerPort());
        sinfo.setServerType(ServerEnum.GAME_SERVER);
        sinfo.setVersion(GameServer.version);
        sinfo.setServerOpentime(ServerConfig.getServerOpenTime());
        sinfo.setServerWorldlv(GlobalType.getWorldLevel());

        G2FReqRegister.Builder msg = G2FReqRegister.newBuilder();
        msg.setSinfo(sinfo);
        send_to_fight(session, -1, G2FReqRegister.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //注册的战斗服返回给游戏服的信息
    public void Register(int sid, ChannelHandlerContext session) {
//        int serverId = session.attr(SessionAttribute.CONNECT_SERVER_ID).get();       
        fightConnects.put(sid, session);
    }

    //注销注册
    public void RemoveSession(ChannelHandlerContext session) {
        int serverId = 0;
        if (session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get() != null) {
            serverId = session.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();
        }

        if (serverId < 1) {
            return;
        }

        if (fightConnects.containsKey(serverId)) {
            ChannelHandlerContext ss = fightConnects.get(serverId);
            if (ss == null) {
                return;
            }
            //检查断开连接时的正确性
            if (!session.equals(ss)) {
                log.error("战斗服ID：" + serverId + " 新旧连接不相同， 不需要进行移除！");
                return;
            }
        }

        fightConnects.remove(serverId);
        log.error("战斗服ID：" + serverId + " 注销了连接了！");
        //重进重新连接
        if (conList.containsKey(serverId)) {
            conList.get(serverId).connect();
        }

//        manager.scriptManager.call(ScriptEnum.ServerScript, "OnFightSessionOut", session, serverId);
        GameServer.getServerScript().OnFightSessionOut(session, serverId);
    }

    private List<ChannelHandlerContext> getSessions() {
        List<ChannelHandlerContext> list = new ArrayList<>();
        list.addAll(fightConnects.values());
        return list;
    }

    public void tick() {
        try {
            List<ChannelHandlerContext> li = getSessions();
            for (final ChannelHandlerContext session : li) {
                ByteBuf sendbuf = null;
                synchronized (session) {
                    if (session.channel() == null) {
                        log.info(session + "发送队列时， 连接已经断开了！1");
                        continue;
                    }

                    if (session.channel().unsafe() == null) {
                        log.info(session.channel() + "发送队列时， 连接已经断开了！2");
                        continue;
                    }
                    if (session.channel().unsafe().outboundBuffer() == null) {
                        log.error(session.channel() + "发送队列时， 连接已经断开了！3");
                        continue;
                    }
                    if (!session.channel().isWritable()) {
                        log.error(session.channel() + "发送队列时，暂时不可写！ size=" + session.channel().unsafe().outboundBuffer().totalPendingWriteBytes());
                        continue;
                    }
                    sendbuf = session.channel().attr(SEND_BUF).get();
                    if (sendbuf != null) {
                        session.channel().attr(SEND_BUF).set(null);
                    }
                }
                try {
                    if (sendbuf != null && sendbuf.readableBytes() > 0) {
                        ChannelFuture cf = session.writeAndFlush(sendbuf);
//                        cf.await();
                    }
                    sendbuf = null;
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        } catch (Exception e) {
            log.error(e, e);//不要影响外部调用
        }
    }

    /**
     * 向所有战斗服发送信息
     *
     * @param msgId
     * @param msg
     */
    public void send_to_allFight(int msgId, byte[] msg) {
        List<ChannelHandlerContext> sessions = new ArrayList<>(fightConnects.values());

        for (ChannelHandlerContext session : sessions) {
            send_to_fight(session, -1, msgId, msg);
        }

    }

    /**
     * 向战斗服发送消息
     *
     * @param sid 战斗目标服
     * @param roleId 战斗角色ID
     * @param msg 战斗的协议内容
     * @param msgId 战斗的消息ID值
     * @return 返回发送是否成功
     */
    public boolean send_to_fight(int sid, long roleId, int msgId, byte[] msg) {
        ChannelHandlerContext session = getFightConnects().get(sid);
        if (session == null) {
            return false;
        }
        if (session.isRemoved()) {
            return false;
        }
        return send_to_fight(session, roleId, msgId, msg);
    }

    public boolean send_to_fight(ChannelHandlerContext session, long roleId, int msgId, byte[] msg) {
        if (session == null) {
            return false;
        }
        ByteBuf buf = null;
        try {
            int len = (Integer.SIZE + Long.SIZE) / Byte.SIZE + msg.length + 1;

            if (msg.length > 512) {
                byte[] zipBytes = ZLibUtils.compress(msg);
                len = (Integer.SIZE + Long.SIZE) / Byte.SIZE + zipBytes.length + 1;
//                int lenlen = len | (((int) 1) << 24);
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                msg = zipBytes;
                buf.writeByte(1);
            } else {
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(0);
            }
            buf.writeInt(msgId);
            buf.writeLong(roleId);
            buf.writeBytes(msg);
            synchronized (session) {
                ByteBuf out = session.channel().attr(SEND_BUF).get();
                if (out == null) {
                    session.channel().attr(SEND_BUF).set(buf);
                } else {
                    out.writeBytes(buf);
                    buf.release();
                }
            }
        } catch (Exception e) {
            log.error("fight 发送处理消息协议时出错了，！", e);
        }

        return true;
    }

    /**
     * 向战斗服转发玩家的协议
     *
     * @param sid 战斗目标服
     * @param roleid 战斗角色ID
     * @param msg 战斗的客户端口协议内容
     * @return
     */
    public boolean send_to_fight(int sid, long roleid, RMessage msg) {
        ChannelHandlerContext session = getFightConnects().get(sid);
        if (session == null) {
            return false;
        }
        if (session.isRemoved()) {
            return false;
        }

//        if (roleid > 0) {
//            LOGGER.error("roleId =" + roleid + "，发送消息ID:" + msg.getId() + ", order=" + msg.getOrder() + ",t=" + TimeUtils.Time());
//        }
        ByteBuf buf = null;
        try {
            byte[] data = msg.getByteData();
            int len = (Integer.SIZE + Long.SIZE) / Byte.SIZE + msg.getByteData().length + 1;
//            buf = Unpooled.buffer(len + Integer.SIZE);
            if (msg.getByteData().length > 512) {
                byte[] zipBytes = ZLibUtils.compress(msg.getByteData());
                len = (Integer.SIZE + Long.SIZE) / Byte.SIZE + zipBytes.length + 1;
//                int lenlen = len | (((int) 1) << 24);
                //                int lenlen = len | (((int) 1) << 24);
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                data = zipBytes;
                buf.writeByte(1);
            } else {
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(0);
            }
            buf.writeInt(msg.getId());
            buf.writeLong(roleid);
            buf.writeBytes(data);
            synchronized (session) {
                ByteBuf out = session.channel().attr(SEND_BUF).get();
                if (out == null) {
                    session.channel().attr(SEND_BUF).set(buf);
                } else {
                    out.writeBytes(buf);
                    buf.release();
                }
            }
        } catch (Exception e) {
            log.error("fight 发送处理消息协议时出错了，！", e);
        }

        return true;
    }

}
