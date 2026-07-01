/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightserver.message;

import com.game.fightserver.struct.FightSMessage;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.CommonQueue;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.TaskEvent;
import static game.core.util.SessionUtils.SEND_BUF;
import game.core.util.TimeUtils;
import game.core.util.ZLibUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 战斗服发送消息的处理函数
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class FightSMessageHandler implements WorkHandler<TaskEvent<FightSMessage>>, EventHandler<TaskEvent<FightSMessage>> {

    private static final Logger log = LogManager.getLogger(FightSMessageHandler.class);

    private DisruptorOrderPoolExecutor<TaskEvent<FightSMessage>,Long,FightSMessage> disruptorPool;

    public void setDisruptorPool(DisruptorOrderPoolExecutor<TaskEvent<FightSMessage>,Long,FightSMessage> disruptorPool) {
        this.disruptorPool = disruptorPool;
    }

    @Override
    public void onEvent(TaskEvent<FightSMessage> t, long l, boolean bln) throws Exception {
        onEvent(t);
    }

    @Override
    public void onEvent(TaskEvent<FightSMessage> t) throws Exception {
        FightSMessage fsm = t.getObj();
        try {
            int type = fsm.getType();
            if (type == 1) {
                sendToGame(fsm.getSession(), fsm.getMess(), fsm.getMsgid(), fsm.getSrcId());
            } else if (type == 2) {
                sendToPlayer(fsm.getLengthWithRole(), fsm.getRoleids(), fsm.getSession(), fsm.getMess(), fsm.getMsgid(), fsm.getSrcId());
            }
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            t.setObj(null);
            t.setType(0);
        }

        if (disruptorPool == null) {
            return;
        }

        afterEvent(fsm);
    }

    public void sendToGame(ChannelHandlerContext session, byte[] mess, int msgid, long srcId) {
        if (session == null) {
            log.error("游戏服连接失败了， 发送协议msg.id=" + msgid + "失败了！");
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
            log.error("向游戏服发送消息协议时出错了，！", e);
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
            log.error("world处理消息协议时出错了，！", e);
        }

    }

    private void afterEvent(FightSMessage fsm) {
        long fromId = fsm.getSrcId();
        try {
            CommonQueue<FightSMessage> queue = this.disruptorPool.getTasksQueue(fromId);
            if (queue == null) {
                return;
            }

            FightSMessage sm = queue.getNewValue();
            if (sm == null) {
                return;
            }

//        logger.info("发送消息ID=" + sm.getId());
            //压入新的队列
            this.disruptorPool.publishNext(fromId, sm);
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
