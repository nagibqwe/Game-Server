package com.game.server843;

import game.core.util.SessionUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * 用于监听页游戏端的数据读取
 *
 * @author soko <xuchangming@haowan123.com>
 */
@ChannelHandler.Sharable
public class Listen843Handler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() < 1) {
            return;
        }

        ByteBuf fff = Unpooled.buffer(buf.readableBytes());
        fff.writeBytes(buf);
        byte[] data = ByteBufUtil.getBytes(fff);
        String mess = new String(data);
        if (mess.equalsIgnoreCase("<policy-file-request/>")) {
            String txt = "<?xml version=\"1.0\"?>\n"
                    + "	<cross-domain-policy>\n"
                    + "	   <allow-access-from domain=\"*\" to-ports=\"1-65536\"/>\n"
                    + "	</cross-domain-policy>";
            ByteBuf bb = Unpooled.buffer(txt.length());
            bb.writeBytes(txt.getBytes());
            ctx.writeAndFlush(bb);
            bb.release();
        } else {
            System.out.println("收到了其它信息 " + (String) msg);
        }
        fff.release();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        if (cause instanceof ReadTimeoutException) {
            System.out.println(ctx.channel().remoteAddress() + "　远程连接30秒验证超时，关闭了！");
        } else {
            System.out.println(ctx.channel().remoteAddress() + " ClientHandlerAdapter 远程关闭了！");
        }
//        cause.printStackTrace();
//        ctx.close();
        SessionUtils.closeSession(ctx, cause.getMessage());
    }
}
