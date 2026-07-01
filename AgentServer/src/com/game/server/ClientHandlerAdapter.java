/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server;

import com.lmax.disruptor.dsl.ProducerType;
import game.core.disruptor.DisruptorNoOrderPoolExecutor;
import game.core.disruptor.MessageEvent;
import game.core.disruptor.MessageEventFactory;
import game.core.disruptor.MessageEventTranslator;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.internal.ConcurrentSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 客户端处理逻辑出入口
 * 九  零一起 玩www.901  75.c  om
 * @author soko <xuchangming@haowan123.com>
 */
public class ClientHandlerAdapter extends SimpleChannelInboundHandler<RMessage> {

    public static final AttributeKey<ByteBuf> SendBuff = AttributeKey.newInstance("SENDBUFF");
    public static final AttributeKey<Integer> SESSIONID = AttributeKey.newInstance("SESSIONID");

    private static final AtomicInteger sessionAutoId = new AtomicInteger();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!ctxCache.contains(ctx)) {
            ctxCache.add(ctx);
            sessionAutoId.getAndIncrement();
            ctx.channel().attr(SESSIONID).set(sessionAutoId.intValue());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx); //To change body of generated methods, choose Tools | Templates.
        if (ctx.channel().isActive()) {
            ctx.close();
        }
        if (ctxCache.contains(ctx)) {
            ctxCache.remove(ctx);
        }
        log.info(ctx.channel().remoteAddress() + "　连接关闭！");
    }

    private static final Logger log = LogManager.getLogger(ClientHandlerAdapter.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            log.error(ctx.channel().remoteAddress() + "　远程连接30秒验证超时，关闭了！");
        } else {
            log.error(ctx.channel().remoteAddress() + " ClientHandlerAdapter 远程关闭了！");
        }
//        if (ctxCache.contains(ctx)) {
        //           ctxCache.remove(ctx);
        //     }
        //s   ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RMessage msg) throws Exception {
        msg.setContext(ctx);
        decodeExcutor.publishEvent(ctx, msg);
    }

//    private static final OrderedQueuePoolExecutor decodeExcutor = new OrderedQueuePoolExecutor("网关消息解析队列", 100, -1);
    private static final DisruptorNoOrderPoolExecutor<MessageEvent, ChannelHandlerContext, RMessage> decodeExcutor = new DisruptorNoOrderPoolExecutor<>(new MessageEventFactory(), 1024 * 1024, ProducerType.MULTI, new RMessageWorkHandler(), 16, new MessageEventTranslator());

    public static void start() {
        decodeExcutor.start();
    }

    public static void stop() {
        decodeExcutor.stop();
    }

    public static void SendMessage(ChannelHandlerContext ctx, SMessage msg) {
        ByteBuf buf = Unpooled.buffer();
        try {
            // 是否压缩
            if (msg.getData().length > 512) {
                byte[] zipBytes = ZLibUtils.compress(msg.getData());
                int lenlen = (zipBytes.length + Integer.SIZE / Byte.SIZE) | (((int) 1) << 24);
                buf.writeInt(lenlen);
                buf.writeInt(msg.getId());
                buf.writeBytes(zipBytes);
            } else {
                buf.writeInt(msg.getData().length + Integer.SIZE / Byte.SIZE);
                buf.writeInt(msg.getId());
                buf.writeBytes(msg.getData());
            }
            ByteBuf out = null;
            synchronized (ctx) {
                out = ctx.channel().attr(SendBuff).get();
                if (out == null) {
                    out = Unpooled.compositeBuffer(buf.writableBytes() + 1);
                    ctx.channel().attr(SendBuff).set(out);
                }
                out.writeBytes(buf);
            }

        } catch (Exception e) {
            log.error(e, e);
        }
        buf.release();
    }

    private static final ConcurrentSet<ChannelHandlerContext> ctxCache = new ConcurrentSet<>();

    /**
     * 发送队列
     */
    public static void Send() {
        for (ChannelHandlerContext ctx : ctxCache) {
            ByteBuf sendbuf;
            synchronized (ctx) {
                sendbuf = ctx.channel().attr(SendBuff).get();
                if (sendbuf != null) {
                    ctx.channel().attr(SendBuff).set(null);
                }
            }
            try {
                if (sendbuf != null && sendbuf.readableBytes() > 0) {
                    ChannelFuture cf = ctx.writeAndFlush(sendbuf);
                    cf.await();
                }
                sendbuf = null;
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

}
