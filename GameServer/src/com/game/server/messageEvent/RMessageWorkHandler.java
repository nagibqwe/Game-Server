/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.messageEvent;

import com.game.server.GameServer;
import com.game.thread.DispatchProcessor;
import com.game.thread.FightServerProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.CommonQueue;
import game.core.disruptor.DisruptorOrderPoolExecutor;
import game.core.disruptor.MessageEvent;
import game.core.message.RMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RMessageWorkHandler implements WorkHandler<MessageEvent>, EventHandler<MessageEvent> {

    private final static Logger logger = LogManager.getLogger(RMessageWorkHandler.class);

    private DisruptorOrderPoolExecutor<MessageEvent, ChannelHandlerContext, RMessage> disruptorPool;

    public void setDisruptorPool(DisruptorOrderPoolExecutor<MessageEvent, ChannelHandlerContext, RMessage> disruptorPool) {
        this.disruptorPool = disruptorPool;
    }

    @Override
    public void onEvent(MessageEvent t, long l, boolean bln) throws Exception {
        onEvent(t);
    }

    @Override
    public void onEvent(MessageEvent t) throws Exception {
        ChannelHandlerContext session = t.getSession();
        try {

            t.getRmess().setContext(t.getSession());
//            logger.info("处理收到消息ID=" + t.getRmess().getId());
            if (GameServer.getInstance().IsFightServer()) {
                FightServerProcessor.getInstance().doAction(t.getRmess());
            } else {
                DispatchProcessor.getInstance().deal().dispatch(t.getRmess());
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            t.setRmess(null);
            t.setSession(null);
        }

        if (null == disruptorPool) {
            return;
        }

        afterEvent(session);
    }

    private void afterEvent(ChannelHandlerContext session) {
        
        if (session.isRemoved()) {
            this.disruptorPool.removeDataCache(session);
            return;
        }
        
        try {
            CommonQueue<RMessage> queue = this.disruptorPool.getTasksQueue(session);
            if (queue == null) {
                return;
            }

            RMessage sm = queue.getNewValue();
            if (sm == null) {
                return;
            }
//            logger.info("处理消息ID=" + sm.getId());
            //压入新的队列
            this.disruptorPool.publishNext(session, sm);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
