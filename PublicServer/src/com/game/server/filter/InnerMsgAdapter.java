/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.filter;

import com.game.manager.Manager;
import com.game.server.MainServer;
import com.lmax.disruptor.dsl.ProducerType;
import game.core.disruptor.DisruptorNoOrderPoolExecutor;
import game.core.disruptor.MessageEvent;
import game.core.disruptor.MessageEventFactory;
import game.core.disruptor.MessageEventTranslator;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import game.message.CrossServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 网络消息处理接口，及网络队列连接数据管理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerMsgAdapter extends SimpleChannelInboundHandler<RMessage> {

    private final static Logger log = LogManager.getLogger(InnerMsgAdapter.class);
    private final static AttributeKey<Integer> SESSIONID = AttributeKey.newInstance("SESSIONID");
    private final static AtomicInteger orderId = new AtomicInteger(1);
//    private static final OrderedQueuePoolExecutor recvExcutor = new OrderedQueuePoolExecutor("消息接收队列", 64, -1);

    private static final DisruptorNoOrderPoolExecutor<MessageEvent, ChannelHandlerContext, RMessage> decodeExcutor = new DisruptorNoOrderPoolExecutor<>(new MessageEventFactory(), ProducerType.MULTI, new RMessageWorkHandler(), new MessageEventTranslator());

    public static void start() {
        decodeExcutor.start();
    }

    public static void stop() {
        decodeExcutor.stop();
    }

    /**
     * 消息接收
     *
     * @param ctx 连接
     * @param msg 消息体
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RMessage msg) throws Exception {
        Integer sessionId = ctx.channel().attr(SESSIONID).get();
        msg.setContext(ctx);

        //心跳包的处理
        if (msg.getId() == CrossServerMessage.G2PConnectHeart.MsgID.eMsgID_VALUE) {
            msg.action();
//            Dictionary dic = MessageDictionary.getInstance().getDictionary(msg.getId());
//            if (dic != null) {
//                try {
//                    Handler handler = dic.getHandlerInstance();
//                    if (handler != null) {
////                        msg.setData(dic.getMessage(), msg.getByteData());
//                        handler.setMessage(msg);
//                        handler.action();
//                    }
//                } catch (InstantiationException | IllegalAccessException ex) {
//                    log.error("inner 转换成handler出错了", ex);
//                }
//            }
            return;
        }

        msg.setExecutor(sessionId);
        decodeExcutor.publishEvent(ctx, msg);
//        recvExcutor.addTask(sessionId, new InnerWorker(sessionId, msg));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().attr(SESSIONID).set(orderId.getAndIncrement());
        log.info("游戏服开始连接" + ctx.name() + " :" + ctx.channel() + " 连接的序号是：" + ctx.channel().attr(SESSIONID).get());
        synchronized (obj) {
            if (!sessions.contains(ctx)) {
                sessions.add(ctx);
                loginfo("开始连接注册成功！");
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("游戏服首次来注册" + ctx.name() + " :" + ctx.channel());
        synchronized (obj) {
            if (!sessions.contains(ctx)) {
                sessions.add(ctx);
                loginfo("首次连接注册成功！");
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SessionUtils.release(ctx.channel());
        log.info("游戏服断开连接" + ctx.name() + " :" + ctx.channel());
        synchronized (obj) {
            if (sessions.contains(ctx)) {
                sessions.remove(ctx);
                loginfo("channelInactive 注销连接!");
            }
        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        SessionUtils.release(ctx.channel());
        log.info("游戏服退出注册" + ctx.name() + " :" + ctx.channel());
        synchronized (obj) {
            if (sessions.contains(ctx)) {
                sessions.remove(ctx);
                loginfo("unreg 注销连接!");
            }
        }
        MainServer.getInstance().SessionQuit(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        log.info("游戏服" + ctx.name() + " :" + ctx.channel() + " 发生了异常===" + cause);
        //出错就主动断开
        if (ctx.channel().isActive()) {
            ctx.channel().unsafe().closeForcibly();//主动断开
            ctx.close();
        }
//        MainServer.getInstance().SessionQuit(ctx);
    }

    private void loginfo(String str) {
        log.info(str);
    }

    /**
     * 消息收集处理中心
     */
    private final static Object obj = new Object();
    private final static List<ChannelHandlerContext> sessions = new ArrayList<>();

    /**
     * 处理发送缓存队列的接口
     */
    public static void BufferSend() {
        List<ChannelHandlerContext> ol = new ArrayList<>();
        synchronized (obj) {
            ol.addAll(sessions);
        }

        //发送消息
        ByteBuf buf;
        for (ChannelHandlerContext ctx : ol) {
            try {
                buf = null;
                synchronized (ctx) {
                    if (ctx.channel() == null) {
                        log.info(ctx + "发送队列时， 连接已经断开了！1");
                        continue;
                    }

                    if (ctx.channel().unsafe() == null) {
                        log.info(ctx.channel() + "发送队列时， 连接已经断开了！2");
                        continue;
                    }

                    if (!ctx.channel().isActive()) {
                        log.info(ctx.channel() + "发送队列时， 连接已经断开了！4");
                        ctx.channel().attr(SessionUtils.SEND_BUF).set(null);
                        continue;
                    }

                    if (ctx.channel().unsafe().outboundBuffer() == null) {
                        log.info(ctx.channel() + "发送队列时， 连接已经断开了！3");
                        continue;
                    }
                    if (!ctx.channel().isWritable()) {
                        log.info(ctx.channel() + "发送队列时，暂时不可写！ size=" + ctx.channel().unsafe().outboundBuffer().totalPendingWriteBytes());
                        continue;
                    }
                    buf = ctx.channel().attr(SessionUtils.SEND_BUF).get();
                    if (buf != null) {
                        ctx.channel().attr(SessionUtils.SEND_BUF).set(null);
                    }
                }
                if (buf != null && ctx.channel().isActive()) {
                    ChannelFuture cf = ctx.writeAndFlush(buf);
                }
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

    //连接数
    public static int getSessionSize() {
        return sessions.size();
    }

}
