/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightserver.manager;

import com.game.fightserver.message.FightSMessageEventTranslator;
import com.game.fightserver.message.FightSMessageHandler;
import com.game.fightserver.struct.FightClient;
import com.game.fightserver.struct.FightSMessage;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.server.GameServer;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.TaskEvent;
import game.core.disruptor.TaskEventFactory;
import game.core.net.Config.ServerConfig;
import game.core.util.SessionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static game.core.util.SessionUtils.SEND_BUF;

/**
 * 战斗服的连接客户管理器
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightClientManager {

    private final static Logger log = LogManager.getLogger(FightClientManager.class);

    private static DisruptorOrderPoolExecutor<TaskEvent<FightSMessage>, Long, FightSMessage> sendExc = null;

    public synchronized void RemoveSession(ChannelHandlerContext ctx) {
        try {
            Iterator<Entry<String, ChannelHandlerContext>> iter = getClientServerList().entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, ChannelHandlerContext> en = iter.next();
                if (en.getValue().equals(ctx)) {
                    getClientServerList().remove(en.getKey());
                    Manager.playerManager.managerExt().removePlayerSession(en.getKey());
                    log.error("sid = " + en.getKey() + "与战斗服断开了连接！");
                    return;
                }
            }
        } catch (Exception e) {
            log.error("断开连接清理注册时出错了，" + e, e);
        }
    }

    public void send_to_game(List<Player> lone, int eMsgID_VALUE, byte[] toByteArray) {
        if (lone.isEmpty()) {
            return;
        }
        Set<ChannelHandlerContext> list = new HashSet<>();
        for (Player player : lone) {
            list.add(player.getIosession());
        }

        if (list.size() < 1) {
            return;
        }

        //向各游戏服发送消息
        for (ChannelHandlerContext iosession : list) {
            send_to_game(iosession, ServerConfig.getServerId(), eMsgID_VALUE, toByteArray);
        }
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        FightClientManager processor;

        Singleton() {
            this.processor = new FightClientManager();
        }

        FightClientManager getProcessor() {
            return processor;
        }
    }

    /**
     * 类单例
     *
     * @return
     */
    public static FightClientManager GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private final ConcurrentHashMap<String, ChannelHandlerContext> clientServerList = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, Long> deleteMapIds = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, ChannelHandlerContext> getClientServerList() {
        return clientServerList;
    }

    private final ConcurrentHashMap<Long, Integer> roomStateCache = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Integer> getRoomStateCache() {
        return roomStateCache;
    }

    public String makeKey(int sid, String platform) {
        return platform + "_" + sid;
    }

    //注册与注销服务器连接
    public synchronized void add(int sid, String plat, ChannelHandlerContext session) {
        String key = makeKey(sid, plat);

        if (session == null) {
            clientServerList.remove(key);
            Manager.playerManager.managerExt().removePlayerSession(key);
            log.error("注销游戏服的连接成功！" + key);
            return;
        }
        if (clientServerList.containsKey(key)) {
            ChannelHandlerContext ss = clientServerList.get(key);
            ss.channel().attr(SessionAttribute.SERVER_ID).set(0);
            ss.channel().attr(SessionAttribute.PLATFORMNAME).set("");
            //将老的标志去掉
        }

        //注册服务器ID， 及平台名字
        session.channel().attr(SessionAttribute.SERVER_ID).set(sid);
        session.channel().attr(SessionAttribute.PLATFORMNAME).set(plat);
        clientServerList.put(key, session);
        Manager.playerManager.managerExt().addPlayerSession(session, key);
        log.error("注册游戏服的连接成功！" + session.channel());
    }
    
    //获取连接会话
    public ChannelHandlerContext getSession(String platSid) {
        if (clientServerList.containsKey(platSid)) {
            return clientServerList.get(platSid);
        }
        return null;
    }
    //获取连接会话
    public ChannelHandlerContext getSession(int sid, String plat) {
        String key = makeKey(sid, plat);
        if (clientServerList.containsKey(key)) {
            return clientServerList.get(key);
        }
        return null;
    }

    //获取所有的会话
    public List<ChannelHandlerContext> getSessions() {
        List<ChannelHandlerContext> list = new ArrayList<>();
        list.addAll(clientServerList.values());
        return list;
    }

    //根据平台获取所有连接
    public List<ChannelHandlerContext> getSessions(String plat) {
        List<ChannelHandlerContext> list = new ArrayList<>();
        list.addAll(clientServerList.values());
        if (list.size() < 1) {
            return list;
        }
        Iterator<Entry<String, ChannelHandlerContext>> iter = clientServerList.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, ChannelHandlerContext> en = iter.next();

//            if (en.getKey().startsWith(plat + "_") == false) {
//                list.remove(en.getValue());
//            }
            String platstr = en.getValue().channel().attr(SessionAttribute.PLATFORMNAME).get();
            if (false == plat.equalsIgnoreCase(platstr)) {
                list.remove(en.getValue());
            }
        }
        return list;
    }

    //所有连接的消息发送
    public void Tick() {
        try {
            checkeMapIdOver();
        } catch (Exception e) {
            log.error(e, e);
        }
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

                    if (!session.channel().isActive()) {
                        log.info(session.channel() + "发送队列时， 连接已经断开了！4");
//                        session.channel().attr(SEND_BUF).set(null);
                        SessionUtils.release(session.channel());
                        continue;
                    }

                    if (session.channel().unsafe().outboundBuffer() == null) {
                        log.error(session.channel() + "发送队列时， 连接已经断开了！3");
                        SessionUtils.release(session.channel());
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
     * 向游戏服写消息
     *
     * @param sid 服务器Id
     * @param plat 平台名
     * @param msgId 信息ID
     * @param msg 信息体
     */
    public void send_to_game(int sid, String plat, int msgId, byte[] msg) {
        ChannelHandlerContext session = getSession(sid, plat);
        if (session == null) {
            log.error("向平台" + plat + "游戏 Id:" + sid + "发送消息时， 游戏服已经不存在连接了！");
            return;
        }
        System.out.println("F>>>>>>>>G消息ID："+msgId);
        send_to_game(session, ServerConfig.getServerId(), msgId, msg);
//        sendToGame(session, new SMessage(msgId, msg));
    }

    /**
     * 向游戏服写消息
     *
     * @param session
     * @param msgId 信息ID
     * @param msg 信息体
     * @return
     */
    public boolean send_to_game(ChannelHandlerContext session, int msgId, byte[] msg) {

        if (session == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }

        if (session.channel() == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }
//        int sid = 0;
//        if (session.channel().hasAttr(SEND_BUF)) {
//            sid = session.channel().attr(SessionAttribute.SERVER_ID).get();
//        }
        FightSMessage fsm = new FightSMessage(null, msgId, msg, ServerConfig.getServerId(), session, 1);
        sendExc.publishEvent((long) ServerConfig.getServerId(), fsm);
//        GameServer.getSendExcutor().addTask(sid, new FightSendGameWorker(msgId, msg, ServerConfig.getServerId(), session));
        return true;
//        return sendToGame(session, new SMessage(msgId, msg));
    }

    /**
     * 向游戏服写消息
     *
     * @param session
     * @param srcId 来源者
     * @param msgId 信息ID
     * @param msg 信息体
     * @return
     */
    public boolean send_to_game(ChannelHandlerContext session, long srcId, int msgId, byte[] msg) {

        if (session == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }

        if (session.channel() == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }
//        int sid = 0;
//        if (session.channel().hasAttr(SEND_BUF)) {
//            sid = session.channel().attr(SessionAttribute.SERVER_ID).get();
//        }
        FightSMessage fsm = new FightSMessage(null, msgId, msg, srcId, session, 1);
        sendExc.publishEvent(srcId, fsm);
//        GameServer.getSendExcutor().addTask(sid, new FightSendGameWorker(msgId, msg, srcId, session));
        return true;
//        return sendToGame(session, new SMessage(msgId, msg));
    }

    /**
     * 向游戏服写消息
     *
     * @param session
     * @param roleids 角色信息列表
     * @param msgId 信息ID
     * @param msg 信息体
     * @param fromId 发起的唯一ID， 比如地图ID， 副本ID
     */
    public void send_to_player(ChannelHandlerContext session, List<Long> roleids, int msgId, byte[] msg, long fromId) {
        if (session == null) {
            log.error("向游戏服" + Arrays.toString(roleids.toArray()) + "发送消息时， 游戏服已经不存在连接了！");
            return;
        }
        sendtoplayer(session, roleids, msgId, msg, fromId);
    }

    /**
     * 向游戏服写消息
     *
     * @param sid 服务器Id
     * @param plat 平台名
     * @param roleids 角色信息列表
     * @param msgId 信息ID
     * @param msg 信息体
     * @param fromId 发起的唯一ID， 比如地图ID， 副本ID
     */
    public void send_to_player(int sid, String plat, List<Long> roleids, int msgId, byte[] msg, long fromId) {
        ChannelHandlerContext session = getSession(sid, plat);
        if (session == null) {
            log.error("向平台" + plat + "游戏 Id:" + sid + "的角色列表" + Arrays.toString(roleids.toArray()) + "发送消息时， 游戏服已经不存在连接了！");
            return;
        }
        sendtoplayer(session, roleids, msgId, msg, fromId);
    }

    /**
     * 向指定的所有玩家发送信息，主要用于地图玩家发送
     *
     * @param players 指定的玩家
     * @param msgId 消息ID
     * @param msg 消息
     * @param fromId 发起的唯一ID， 比如地图ID， 副本ID
     */
    public void send_to_players(Collection<Player> players, int msgId, byte[] msg, long fromId) {
        HashMap<ChannelHandlerContext, List<Long>> sessionList = new HashMap<>();
        for (Player player : players) {
            List<Long> tmp;
            if (sessionList.containsKey(player.getIosession())) {
                tmp = sessionList.get(player.getIosession());
            } else {
                tmp = new ArrayList<>();
                sessionList.put(player.getIosession(), tmp);
            }
            tmp.add(player.getId());
        }

        Iterator<Entry<ChannelHandlerContext, List<Long>>> iter = sessionList.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<ChannelHandlerContext, List<Long>> en = iter.next();
            sendtoplayer(en.getKey(), en.getValue(), msgId, msg, fromId);
        }
    }

    private boolean sendtoplayer(ChannelHandlerContext session, List<Long> roleids, int msgId, byte[] msg, long fromId) {
//        OtherServerToPlayerMessage mess = new OtherServerToPlayerMessage();
//        mess.setId(msgId);
//        mess.setBytes(msg);
//        mess.setSendId(ServerConfig.getServerId());
//        mess.setRoleIds(roleids);
//        mess.setSendTime((int) (TimeUtils.Time() / 1000));
        if (session == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }

        if (session.channel() == null) {
            log.error("向游戏服发送消息时， 游戏服已经不存在连接了！");
            return false;
        }
        int sid = 0;
        if (session.channel().hasAttr(SEND_BUF)) {
            sid = session.channel().attr(SessionAttribute.SERVER_ID).get();
        }
        FightSMessage fsm = new FightSMessage(roleids, msgId, msg, fromId, session, 2);
        sendExc.publishEvent(fromId, fsm);
        //GameServer.getSendExcutor().addTask(sid, new FightSendPlayerWorker(roleids, msgId, msg, session, fromId));
        return true;
    }

    /**
     * 向游戏发送服务器之间的消息
     *
     * @param session
     * @param msg
     * @return
     */
    /**
     * public static boolean sendToGame(ChannelHandlerContext session, SMessage
     * msg) {
     *
     * if (session == null) { LOGGER.error("游戏服连接失败了， 发送协议msg.id=" + msg.getId()
     * + "失败了！"); return false; }
     *
     * ByteBuf buf = Unpooled.buffer(); try { if (msg.getData().length > 512) {
     * byte[] zipBytes = ZLibUtils.compress(msg.getData()); int len =
     * zipBytes.length + (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE;
     * buf.writeInt(len); buf.writeByte(1); buf.writeInt(msg.getId());
     * buf.writeLong(msg.getSender()); buf.writeBytes(zipBytes); } else { int
     * len = (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE +
     * msg.getData().length; buf.writeInt(len); buf.writeByte(0);
     * buf.writeInt(msg.getId()); buf.writeLong(msg.getSender());
     * buf.writeBytes(msg.getData()); } synchronized (session) { ByteBuf out =
     * session.channel().attr(SEND_BUF).get(); if (out == null) { out =
     * Unpooled.compositeBuffer(buf.writableBytes());
     * session.channel().attr(SEND_BUF).set(out); } out.writeBytes(buf); } }
     * catch (Exception e) { LOGGER.error("world处理消息协议时出错了，！", e); }
     * buf.release(); return true; }
     */
    /**
     * 向个人客户端发送消息
     *
     * @param session
     * @param msg
     * @return
     */
    /*
     public static boolean sendToPlayer(ChannelHandlerContext session, OtherServerToPlayerMessage msg) {

     if (session == null) {
     //            LOGGER.error("玩家已经连接失败了， 请确认");
     return false;
     }

     if (session.isRemoved()) {
     return false;
     }

     ByteBuf buf = Unpooled.buffer();
     try {
     byte[] data = msg.getBytes();
     int datalen = data.length;
     if (datalen > 512) {
     byte[] zipBytes = ZLibUtils.compress(msg.getBytes());
     int len = msg.getLengthWithRole() - datalen + zipBytes.length + 1;
     //                int lenlen = len | (((int) 1) << 24);
     buf.writeInt(len);
     buf.writeByte(1);
     data = zipBytes;
     } else {
     buf.writeInt(msg.getLengthWithRole() + 1);
     buf.writeByte(0);
     }
     buf.writeInt(msg.getId());
     buf.writeLong(msg.getSendId());
     buf.writeInt(msg.getSendTime());
     buf.writeInt(msg.getRoleIds().size());
     for (int i = 0; i < msg.getRoleIds().size(); i++) {
     buf.writeLong(msg.getRoleIds().get(i));
     }
     buf.writeBytes(data);
     synchronized (session) {
     ByteBuf out = session.channel().attr(SEND_BUF).get();
     if (out == null) {
     out = Unpooled.compositeBuffer(buf.writableBytes());
     session.channel().attr(SEND_BUF).set(out);
     }
     out.writeBytes(buf);
     }

     } catch (Exception e) {
     LOGGER.error("world处理消息协议时出错了，！", e);
     }
     buf.release();
     return true;
     }*/
    //停止函数
    public void stop() {
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }
        sendExc.stop();
    }

    public void init(int queueSize) {
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }

        TaskEventFactory<FightSMessage> tef = new TaskEventFactory<>();
        FightSMessageEventTranslator fsmet = new FightSMessageEventTranslator();
        FightSMessageHandler fsmh = new FightSMessageHandler();
        sendExc = new DisruptorOrderPoolExecutor<>("FightClient", tef, queueSize, ProducerType.MULTI, fsmh, 64, fsmet);
        fsmh.setDisruptorPool(sendExc);
    }

    //启动函数
    public void start() {
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }
        sendExc.start();
    }

    public void removeMapId(long mapId) {
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }
        deleteMapIds.put(mapId, System.currentTimeMillis());
//        sendExc.removeDataCache(mapId);
    }

    private void checkeMapIdOver() {
        long now = System.currentTimeMillis();
        Iterator<Entry<Long, Long>> iter = deleteMapIds.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, Long> en = iter.next();
            long mapId = en.getKey();
            long offtime = en.getValue();

            if (now - offtime > 30000) {
                sendExc.removeDataCache(mapId);
                iter.remove();
            }
        }
    }

    /**
     * 检查玩家在当前战斗服的来源服务器ID值
     *
     * @param player 玩家
     * @return 返回服务器ID值
     */
    public static int getServerIdInFightServer(Player player) {
        if (player.getIosession() == null) {
            return player.getCreateServerId();
        }

        ChannelHandlerContext context = player.getIosession();
        if (context.channel().hasAttr(SessionAttribute.FIGHT_CLIENT_INFO)) {
            FightClient fc = context.channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc != null) {
                return fc.getSid();
            }
        }
        return player.getCreateServerId();
    }
}
