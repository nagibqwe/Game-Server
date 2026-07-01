/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.MessageEvent;
import game.core.message.Dictionary;
import game.core.message.MessageDictionary;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RMessageWorkHandler implements WorkHandler<MessageEvent>, EventHandler<MessageEvent> {

    private final static Logger logger = LogManager.getLogger(RMessageWorkHandler.class);

    @Override
    public void onEvent(MessageEvent t, long l, boolean bln) throws Exception {
        onEvent(t);
    }

    @Override
    public void onEvent(MessageEvent t) throws Exception {
        try {
            RMessage msg = t.getRmess();
            Dictionary dic = MessageDictionary.getInstance().getDictionary(msg.getId());
            if (dic != null) {
                msg.action();
            } else {
                logger.error("找不到处理此协议的消息ID:" + msg.getId());
            }
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            t.setRmess(null);
            t.setSession(null);
        }
    }
}
