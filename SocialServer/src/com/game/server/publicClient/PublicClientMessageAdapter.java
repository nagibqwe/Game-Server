package com.game.server.publicClient;

import com.game.server.SocialServer;
import com.game.server.timer.PublicServerReconnectTimer;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/6/11 18:23
 * @Auth ZUncle
 */
public class PublicClientMessageAdapter extends SimpleChannelInboundHandler<RMessage> {

    static Logger logger = LogManager.getLogger(PublicClient.class);

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
        SocialServer.getInstance().pc.channel = ctx;
        msg.setContext(ctx);
        msg.setExecutor(ctx);
        SocialServer.getInstance().decodeExc.publishEvent(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocialServer.getInstance().pc.channel = ctx;
        logger.info("公共服激活连接{}: {} ", ctx.name(), ctx.channel());
        SocialServer.getInstance().server().register2Public();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        logger.info("公共服连接注册{}: {} ", ctx.name(), ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        SessionUtils.release(ctx.channel());
        SocialServer.getInstance().pc.channel = null;
        SocialServer.getInstance().addCommand(new PublicServerReconnectTimer());
//        logger.info("公共服连接注销{}: {} ", ctx.name(), ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("公共服{}: {} 发生了异常：{}", ctx.name(), ctx.channel(), cause);
    }
}
