/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.net.server.SocketServer;
import game.core.script.ScriptManager;
import game.core.util.SessionUtils;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.AttributeKey;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 处理网络的解包与压包的问题
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ClientMessageCodeC extends ByteToMessageCodec<SMessage>
{

    private static final AttributeKey<Long> START_TIME = AttributeKey.newInstance("START_TIME");
    private static final AttributeKey<Integer> RECEIVE_COUNT = AttributeKey.newInstance("RECEIVE_COUNT");
    private static final List<Integer> ignoreMessage = new ArrayList<>();
    private static final AttributeKey<Integer> LAST_RECEIVED_TIME = AttributeKey.newInstance("LAST_RECEIVED_TIME");
    private static final AttributeKey<Integer> PRE_ORDER = AttributeKey.newInstance("PRE_ORDER");

    private static final Logger log = LogManager.getLogger(ClientMessageCodeC.class);

    public static List<Integer> getIgnoreMessage()
    {
        return ignoreMessage;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SMessage msg, ByteBuf out) throws Exception
    {
        ByteBuf buf = ctx.alloc().buffer();
        // 是否压缩
        if (msg.getData().length > 512)
        {
            byte[] zipBytes = ZLibUtils.compress(msg.getData());
            int lenlen = (zipBytes.length + Integer.SIZE / Byte.SIZE) | (((int) 1) << 24);
            buf.writeInt(lenlen);
            buf.writeInt(msg.getId());
            buf.writeBytes(zipBytes);
        }
        else
        {
            buf.writeInt(msg.getData().length + Integer.SIZE / Byte.SIZE);
            buf.writeInt(msg.getId());
            buf.writeBytes(msg.getData());
        }
        out.writeBytes(buf);
        buf.release();
    }

    private int maxcount = 30;

    public ClientMessageCodeC(int maxCount)
    {
        this.maxcount = maxCount;
    }

    /**
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {

        if (ctx.isRemoved())
            return;

        int mseLen = in.readableBytes();
        if (mseLen < 1)
            return;

        int length = in.readInt();
        if (length < 1)
        {
            log.error("当前Session 发上来的数据异常！ " + ctx + "发送了一个空包， 需要踢下线处理吗？");
            return;
        }
//            log.error(" buf read len =" + ableRead + "  packet len =" + length + ", get len =" + len + " readable" + in.readableBytes());
        ByteBuf buf = in;//
        int orderId = buf.readInt();
        int sercertKey = orderId % 100;
        orderId = orderId ^ (SocketServer.getNetCodeKey() << 10);
        orderId = orderId ^ length;

//        System.out.println("length = " + length + "orderId=" + orderId + " ,key=" + sercertKey);
        int totalCode = buf.readInt();
        int upCode = 0;
        for (int i = buf.readerIndex(); i < buf.writerIndex(); ++i)
        {
//            System.out.print(buf.getByte(i) + "=");
            int code = buf.getByte(i) ^ sercertKey;
            buf.setByte(i, code);
//            System.out.print(code);
//            System.out.print("(");
            if (code < 0)
            {
                code = (256 + code);
            }
//            System.out.print(code + ") ");

            upCode += code;
        }
//        System.out.println();

        if (totalCode != upCode)
        {
            String str = String.format("当前Session 发上来的数据异常！ totalCode =%d != %d", totalCode, upCode);
            log.error(str);
            //断开网络连接
//            ctx.channel().unsafe().closeForcibly();
//            ctx.channel().close();
            SessionUtils.closeSession(ctx, str);
            return;
        }
        int curTimes = buf.readInt();
        int msgId = buf.readInt();

        long startTime = 0;

        if (ctx.channel().attr(START_TIME).get() != null)
        {
            startTime = ctx.channel().attr(START_TIME).get();
        }
        int count = 0;
        if (ctx.channel().attr(RECEIVE_COUNT).get() != null)
        {
            count = ctx.channel().attr(RECEIVE_COUNT).get();
        }
        long offset = System.currentTimeMillis() - startTime;
        if (offset > 1000)
        {
            if (count > 30)
            {
                log.error(ctx.channel() + " --> sendmsg:" + count + " msgId:" + msgId + " , time:" + offset);
            }
            startTime = System.currentTimeMillis();
            count = 0;
        }
        count++;
        if (count > 500)
        {
            // 如果客户端在1秒之内发送了超过MAX_COUNT条消息, 则认为客户端在恶意刷包, 断开其连接
            log.error(ctx.channel() + " --> sendmsg:" + count + "-->close-->buffer:"
                    + ")");
            SessionUtils.closeSession(ctx, "客户端发送的消息次数太频繁(" + count + ")" + " msgId:" + msgId);
            return;
        }
        else
        {
            ctx.channel().attr(START_TIME).set(startTime);
            ctx.channel().attr(RECEIVE_COUNT).set(count);
        }

        if (ctx.channel().attr(LAST_RECEIVED_TIME).get() != null)
        {
            int lastTimes = ctx.channel().attr(LAST_RECEIVED_TIME).get();
            if (lastTimes > curTimes)
            {
                // 是否可以忽略的消息
                if (!ignoreMessage.contains(msgId))
                {
                    log.error(ctx.channel() + "时间戳出错: message id = " + msgId + ", lastTimes = " + lastTimes + ", curTimes = " + curTimes);
                    return;
                }
                else
                {
                    log.info(ctx.channel() + "未丢弃的过期包： message id = " + msgId + ", lastTimes = " + lastTimes + ", curTimes = " + curTimes);
                }
            }
        }

        int preOrder = 0;
        if (ctx.channel().attr(PRE_ORDER).get() != null)
        {
            preOrder = ctx.channel().attr(PRE_ORDER).get();
        }

        ctx.channel().attr(LAST_RECEIVED_TIME).set(curTimes);

        if (orderId < preOrder)
        {
            // 是否可以忽略的消息
            if (!ignoreMessage.contains(msgId))
            {
                log.error(ctx + "包序列出错: message id = " + msgId + ", order = " + orderId + ", preOrder = " + preOrder);
                SessionUtils.closeSession(ctx, "包序列出错");
                return;
            }
            else
            {
                log.info(ctx + "未丢弃的错误序列包：message id = " + msgId + ", order = " + orderId + ", preOrder = " + preOrder);
            }
        }

        //重新将序号加上处理
        if (orderId != preOrder)
        {
            log.info(ctx + "出现的错误序列包：message id = " + msgId + ", order = " + orderId + ", preOrder = " + preOrder);
            preOrder = orderId;
        }

        ctx.channel().attr(PRE_ORDER).set(preOrder + 1);

        // 创建消息对象
        RMessage message = new RMessage();
        message.setId(msgId);
        message.setOrder(orderId);
        message.setTimes(curTimes);
        ByteBuf fff = Unpooled.buffer(buf.readableBytes());
        fff.writeBytes(buf);
        byte[] data = ByteBufUtil.getBytes(fff);
        try
        {
            // 获取消息字典
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
            if (dic != null)
            {
                message.setData(dic.getMessage(), data);
                message.setByteData(data);
            }
            else
            {
                log.error("Not found message handler! msgId = " + msgId + " orderId=" + orderId);
            }
        }
        catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            log.error(Arrays.toString(data));
            log.error("msgID " + msgId + " 的packet error! order " + orderId + e, e);
            ScriptManager.getInstance().error("msgID " + msgId + " 的packet error! order " + orderId + e, Arrays.toString(data));
        }
        out.add(message);
        fff.release();
    }

}
