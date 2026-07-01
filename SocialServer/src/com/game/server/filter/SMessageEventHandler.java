package com.game.server.filter;

import com.game.server.struct.MessageEvent;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.CommonQueue;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.message.SMessage;
import game.core.util.TimeUtils;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static game.core.util.SessionUtils.SEND_BUF;

/**
 * @Desc TODO
 * @Date 2021/6/9 14:23
 * @Auth ZUncle
 */
public class SMessageEventHandler implements WorkHandler<MessageEvent<ChannelHandlerContext, SMessage>>, EventHandler<MessageEvent<ChannelHandlerContext, SMessage>> {

    static Logger logger = LogManager.getLogger(SMessageEventHandler.class);

    DisruptorOrderPoolExecutor<MessageEvent<ChannelHandlerContext, SMessage>, ChannelHandlerContext, SMessage> executor;

    public void setExecutor(DisruptorOrderPoolExecutor<MessageEvent<ChannelHandlerContext, SMessage>, ChannelHandlerContext, SMessage> executor) {
        this.executor = executor;
    }

    @Override
    public void onEvent(MessageEvent<ChannelHandlerContext, SMessage> event, long l, boolean b) throws Exception {
        onEvent(event);
    }

    @Override
    public void onEvent(MessageEvent<ChannelHandlerContext, SMessage> event) throws Exception {
        ChannelHandlerContext channel = event.getKey();
        SMessage fsm = event.getVal();
        try {
            if (fsm.getRecv() == null || fsm.getRecv().isEmpty()) {
                sendToGame(channel, fsm.getData(), fsm.getId(), fsm.getSender());
            } else {
                sendToPlayer(getLengthWithRole(fsm), fsm.getRecv(), channel, fsm.getData(), fsm.getId(), fsm.getSender());
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            event.setKey(null);
            event.setVal(null);
        }

        if (executor == null) {
            return;
        }
        afterEvent(channel);
    }

    public int getLengthWithRole(SMessage message) {
        if (message.getRecv() == null) {
            return message.getData().length + Integer.SIZE / Byte.SIZE + Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE;
        }
        return message.getData().length + Integer.SIZE / Byte.SIZE + Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE + message.getRecv().size() * Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE;
    }

    public void sendToGame(ChannelHandlerContext session, byte[] mess, int msgid, long srcId) {
        if (session == null) {
            logger.error("游戏服连接失败了， 发送协议msg.id=" + msgid + "失败了！");
            return;
        }

        ByteBuf buf = null;
        try {
            if (mess.length > 512) {
                byte[] zipBytes = ZLibUtils.compress(mess);
                int len = zipBytes.length + (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE;
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(1);
                buf.writeInt(msgid);
                buf.writeLong(srcId);
                buf.writeBytes(zipBytes);
            } else {
                int len = (Integer.SIZE + Long.SIZE + Byte.SIZE) / Byte.SIZE + mess.length;
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(0);
                buf.writeInt(msgid);
                buf.writeLong(srcId);
                buf.writeBytes(mess);
            }
            synchronized (session) {
                ByteBuf out = session.channel().attr(SEND_BUF).get();
                if (out == null) {
                    session.channel().attr(SEND_BUF).set(buf);
                } else {
                    out.writeBytes(buf);
                    buf.release();
                }
            }
        } catch (Exception e) {
            logger.error("向游戏服发送消息协议时出错了，！", e);
        }
    }

    public void sendToPlayer(int msglen, List<Long> roleids, ChannelHandlerContext session, byte[] mess, int msgid, long srcId) {
        if (session.isRemoved()) {
            return;
        }
        ByteBuf buf = null;
        try {
            byte[] data = mess;
            int datalen = data.length;
            if (datalen > 512) {
                byte[] zipBytes = ZLibUtils.compress(mess);
                int len = msglen - datalen + zipBytes.length + 1;
//                int lenlen = len | (((int) 1) << 24);
                buf = Unpooled.compositeBuffer(len + Integer.SIZE);
                buf.writeInt(len);
                buf.writeByte(1);
                data = zipBytes;
            } else {
                buf = Unpooled.compositeBuffer(msglen + Integer.SIZE + 1);
                buf.writeInt(msglen + 1);
                buf.writeByte(0);
            }
            buf.writeInt(msgid);
            buf.writeLong(srcId);
            buf.writeInt((int) (TimeUtils.Time() / 1000));
            buf.writeInt(roleids.size());
            for (Long roleid : roleids) {
                buf.writeLong(roleid);
            }
            buf.writeBytes(data);
            synchronized (session) {
                ByteBuf out = session.channel().attr(SEND_BUF).get();
                if (out == null) {
                    session.channel().attr(SEND_BUF).set(buf);
                } else {
                    out.writeBytes(buf);
                    buf.release();
                }
            }
        } catch (Exception e) {
            logger.error("world处理消息协议时出错了，！", e);
        }

    }


    private void afterEvent(ChannelHandlerContext channel) {
        try {
            CommonQueue<SMessage> queue = this.executor.getTasksQueue(channel);
            if (queue == null) {
                return;
            }
            SMessage sm = queue.getNewValue();
            if (sm == null) {
                return;
            }
            //压入新的队列
            this.executor.publishNext(channel, sm);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}
