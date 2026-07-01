package com.game.server.filter;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.QuitGameDefine;
import com.game.player.structs.SessionAttribute;
import com.game.server.GameServer;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

import static com.game.server.GameServer.SESSIONID;

/**
 * 客户端网络信息的接入口
 *
 */
public class ClientMsgAdapter extends SimpleChannelInboundHandler<RMessage> {

    private final static Logger log = LogManager.getLogger("com.game.Register.manager.RegisterManager");
    private final static Logger logger = LogManager.getLogger("HandlerDealTime");

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannels() {
        return channels;
    }

    /**
     * 网络连接异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel session = ctx.channel();
        logger.error("=====in exceptionCaught, session id = " + session + ", exceptionCaught" + cause, cause);
        // SessionUtils.closeSession(ctx, "game 收到玩家错误连接异常！");
    }

    private static final AtomicLong sessionID = new AtomicLong();

    /**
     * 首次连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        ctx.channel().attr(GameServer.SESSIONID).set(sessionID.getAndIncrement());
        log.info(ctx.channel() + ", id=" + ctx.channel().attr(SESSIONID).get().intValue() + "首次来连接");
        super.channelActive(ctx);
    }

    /**
     * 连接注销， 主动与被动都会激活
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        SessionUtils.release(ctx.channel());
        // 服务端内部调用的handler没有消息号，不能由DispatchProcessor分派处理器，直接选择处理器
        Manager.playerManager.iQuitGame().QuitGame(ctx, QuitGameDefine.ClientMsg, false, true);
        super.channelUnregistered(ctx);
        //释放空间
        GameServer.SessionOut(ctx);
    }

    /**
     * 数据读取端口
     *
     * @param chc
     * @param i
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext chc, RMessage i) throws Exception {
        GameServer.decodeExcu(chc, i);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
//        LOGGER.info(ctx.channel() + " 连接的读取已经完成！");
        //super.channelReadComplete(ctx); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (ctx.channel().attr(SessionAttribute.PLAYER).get() != null) {
            Player player = ctx.channel().attr(SessionAttribute.PLAYER).get();
            if (player != null) {
                log.info(ctx.channel() + " 已经注册了 ClientMsgAdapter玩家:" + player.getName() + "(" + player.getId() + ")");
            } else {
                log.info(ctx.channel() + " 连接还没有注册成功玩家信息！ClientMsgAdapter");
            }
        } else {
            log.info(ctx.channel() + " 连接还没有注册成功玩家信息！ClientMsgAdapter");
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        SessionUtils.release(ctx.channel());
        log.info(ctx.channel() + ",id=" + ctx.channel().attr(SESSIONID).get().intValue() + " 连接的已经移除了！");
    }

}
