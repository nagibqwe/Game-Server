package com.game.server.social;

import com.game.server.GameServer;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/6/22 20:05
 * @Auth ZUncle
 */
public class SocialHandlerAdapter extends SimpleChannelInboundHandler<RMessage> {

    static Logger logger = LogManager.getLogger(SocialHandlerAdapter.class);

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
        SocialServerClient.getInstance().channel = ctx;
        msg.setContext(ctx);
        msg.setExecutor(ctx);
        logger.info("社交服 接收消息{}: {}  mess={} ", ctx.name(), ctx.channel(), msg.getId());

        GameServer.worldExec(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocialServerClient.getInstance().channel = ctx;
        logger.info("社交服激活连接{}: {} ", ctx.name(), ctx.channel());
        GameServer.getServerScript().G2SRegisterServer(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        logger.info("社交服连接注册{}: {} ", ctx.name(), ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        SessionUtils.release(ctx.channel());
        SocialServerClient.getInstance().channel = null;
        GameServer.getInstance().getAssistThread().addCommand(new SocialServerReconnectTimer());
//        logger.info("社交服连接注销{}: {} ", ctx.name(), ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("社交服{}: {} 发生了异常：{}", ctx.name(), ctx.channel(), cause);
    }
}
