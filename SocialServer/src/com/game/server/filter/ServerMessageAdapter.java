package com.game.server.filter;

import com.game.server.SocialServer;
import com.game.server.publicClient.PublicClientChannelImpl;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Desc TODO
 * @Date 2021/6/10 17:10
 * @Auth ZUncle
 */
public class ServerMessageAdapter extends SimpleChannelInboundHandler<RMessage> {

    static Logger logger = LogManager.getLogger(PublicClientChannelImpl.class);

    private final static Set<ChannelHandlerContext> sessions = new HashSet<>();
    private final static AttributeKey<Integer> SESSIONID = AttributeKey.newInstance("SESSIONID");
    private final static AtomicInteger orderId = new AtomicInteger(1);

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link RMessage}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RMessage msg) throws Exception {
        msg.setContext(ctx);
        SocialServer.getInstance().decodeExc.publishEvent(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().attr(SESSIONID).set(orderId.getAndIncrement());
        sessions.add(ctx);
        logger.info("游戏服激活连接{}: {} 连接的序号是：{}", ctx.name(), ctx.channel(), ctx.channel().attr(SESSIONID).get());
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        sessions.add(ctx);
        logger.info("游戏服首次来注册{}: {} 连接的序号是：{}", ctx.name(), ctx.channel(), ctx.channel().attr(SESSIONID).get());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SessionUtils.release(ctx.channel());

        sessions.remove(ctx);
        logger.info("游戏服断开连接{}: {} 连接的序号是：{}", ctx.name(), ctx.channel(), ctx.channel().attr(SESSIONID).get());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        SessionUtils.release(ctx.channel());
        sessions.remove(ctx);
        logger.info("游戏服退出注册{}: {} 连接的序号是：{}", ctx.name(), ctx.channel(), ctx.channel().attr(SESSIONID).get());

        SocialServer.getInstance().server().close(ctx, 0);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("游戏服{}: {} 发生了异常：{}", ctx.name(), ctx.channel(), cause);
        //出错就主动断开
        if (ctx.channel().isActive()) {
            ctx.channel().unsafe().closeForcibly();//主动断开
            ctx.close();
        }
    }

    /**
     * 处理发送缓存队列的接口
     */
    public static void BufferSend() {

        List<ChannelHandlerContext> ol = new ArrayList<>(sessions);

        //发送消息
        ByteBuf buf;
        for (ChannelHandlerContext ctx : ol) {
            try {
                synchronized (ctx) {
                    if (ctx.channel() == null) {
                        logger.info(ctx + "发送队列时， 连接已经断开了！1");
                        continue;
                    }

                    if (ctx.channel().unsafe() == null) {
                        logger.info(ctx.channel() + "发送队列时， 连接已经断开了！2");
                        continue;
                    }

                    if (!ctx.channel().isActive()) {
                        logger.info(ctx.channel() + "发送队列时， 连接已经断开了！4");
                        ctx.channel().attr(SessionUtils.SEND_BUF).set(null);
                        continue;
                    }

                    if (ctx.channel().unsafe().outboundBuffer() == null) {
                        logger.info(ctx.channel() + "发送队列时， 连接已经断开了！3");
                        continue;
                    }
                    if (!ctx.channel().isWritable()) {
                        logger.info(ctx.channel() + "发送队列时，暂时不可写！ size=" + ctx.channel().unsafe().outboundBuffer().totalPendingWriteBytes());
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
                logger.error(e, e);
            }
        }
    }


}
