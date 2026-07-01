/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionUtils
{
    public static final AttributeKey<ByteBuf> SEND_BUF = AttributeKey.newInstance("SEND_BUF");
    private static final Logger closelog = LogManager.getLogger("Session");

    public static void closeSession(ChannelHandlerContext ctx, String reason)
    {
        closelog.warn("remote ip:" + ctx.channel().remoteAddress() + " isActive:" + ctx.channel().isActive() + " reason:" + reason);

        if (ctx.channel().isActive())
        {
            ctx.close();
            ctx.channel().close();
            ctx.channel().unsafe().closeForcibly();
        }
    }

    public static ByteBuf getByteBuf(ChannelHandlerContext ctx)
    {
        return getByteBuf(ctx.channel());
    }

    public static ByteBuf getByteBuf(Channel channel)
    {
        if (channel.hasAttr(SEND_BUF))
        {
            return channel.attr(SEND_BUF).get();
        }
        return null;
    }

    public static void release(Channel channel)
    {
        if (channel.hasAttr(SEND_BUF))
        {
            ByteBuf sendbuf = channel.attr(SEND_BUF).get();
            if (sendbuf != null)
            {
                sendbuf.release();
            }
            channel.attr(SEND_BUF).set(null);
        }
    }

    public static void log(String str)
    {
        closelog.error(str, new Throwable("断开日志"));
    }
}
