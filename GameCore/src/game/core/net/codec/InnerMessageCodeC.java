/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.net.codec;

import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.MessageNumber;
import game.core.message.MsgSourceEnum;
import game.core.message.OtherServerToPlayerMessage;
import game.core.message.RMessage;
import game.core.message.SMessage;
import game.core.script.ScriptManager;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import static io.netty.util.ReferenceCountUtil.releaseLater;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 内部连接通信处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerMessageCodeC extends ByteToMessageCodec<SMessage>
{
    private static final Logger log = LogManager.getLogger(InnerMessageCodeC.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, SMessage msg, ByteBuf out) throws Exception
    {
//        ByteBuf buff = ctx.alloc().buffer();
//        int len = (Integer.SIZE + Long.SIZE) / Byte.SIZE + msg.getData().length;
//        buff.writeInt(len);
//        buff.writeInt(msg.getId());
//        buff.writeLong(msg.getSender());
//        buff.writeBytes(msg.getData());
//        out.writeBytes(buff);
//        buff.release();//释放
//        if (ctx.channel().isActive())
//        {
//            ctx.flush();
//        }
//        ctx.writeAndFlush(buff);
//        int ableRead = buff.writerIndex() - buff.readerIndex();//可读长度
//        log.error(" write buf len =" + ableRead + "  packet len =" + len);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 1)
            return;

        int length = in.readInt();
        if (length < 1)
            return;

        //log.error("InnerMessageCodeC    buf read len =" + in.readableBytes() + "  packet len =" + length);
//        ByteBuf buf = in.readBytes(length);
        ByteBuf buf = in.readBytes(length);
//        buf.writeBytes(in.readBytes(length));
        int isZip = buf.readByte();
        int msgId = buf.readInt();
        long srcId = buf.readLong();

        int sourceId = MessageNumber.getSource(msgId);

        //如果是其它服务器发给玩家的
        if (sourceId == MsgSourceEnum.GameServerToClient) {
            int sendTime = buf.readInt();
            int roleNum = buf.readInt();
            List<Long> roles = new ArrayList<>();
            for (int i = 0; i < roleNum; i++) {
                roles.add(buf.readLong());
            }

            OtherServerToPlayerMessage msg = new OtherServerToPlayerMessage();
            msg.setId(msgId);
            msg.setSendTime(sendTime);
            msg.setSendId(srcId);
            msg.setRoleIds(roles);

            try {
                if (isZip > 0) {
                    byte[] data = ByteBufUtil.getBytes(buf);
                    data = ZLibUtils.decompress(data);
                    msg.setBytes(data);
                } else {
                    msg.setBytes(ByteBufUtil.getBytes(buf));
                }
            } catch (Exception e) {
                log.error("msgID " + msgId + " 的packet error!" + e, e);
            }
            out.add(msg);
        } else {
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msgId);
            if (dic == null) {
                log.error("Not found message dictionary! msgId = " + msgId);
                return;
            }
            RMessage message = new RMessage();
            message.setId(msgId);
            message.setOrder(0);
            message.setTimes(0);
            message.setSrcId(srcId);
            message.setContext(ctx);

            try {
                if (isZip > 0) {
                    byte[] data = ByteBufUtil.getBytes(buf);
                    data = ZLibUtils.decompress(data);
                    message.setData(dic.getMessage(), data);
                } else {
                    byte[] msg = ByteBufUtil.getBytes(buf);
                    message.setData(dic.getMessage(), msg);
                }
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("msgID " + msgId + " 的packet error!" + e, e);
            }
            out.add(message);
        }
        buf.release();
    }

}
