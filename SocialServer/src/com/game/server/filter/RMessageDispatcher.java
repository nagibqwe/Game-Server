package com.game.server.filter;

import com.game.server.SocialServer;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import game.core.disruptor.MessageEvent;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/6/8 17:16
 * @Auth ZUncle
 */
public class RMessageDispatcher implements WorkHandler<MessageEvent>, EventHandler<MessageEvent> {

    static Logger logger = LogManager.getLogger(RMessageDispatcher.class);

    @Override
    public void onEvent(MessageEvent messageEvent, long l, boolean b) throws Exception {
        onEvent(messageEvent);
    }

    @Override
    public void onEvent(MessageEvent messageEvent) throws Exception {
        try {
            RMessage msg = messageEvent.getRmess();
            SocialServer.getInstance().server().dispatch(msg);
        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            messageEvent.setRmess(null);
            messageEvent.setSession(null);
        }
    }
}
