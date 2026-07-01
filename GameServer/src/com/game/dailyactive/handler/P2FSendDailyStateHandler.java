package com.game.dailyactive.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.DailyactiveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.P2FSendDailyState;


/**
 * makehandler  v1.9 for netty
 */
@Message(id = DailyactiveMessage.P2FSendDailyState.MsgID.eMsgID_VALUE, clazz = DailyactiveMessage.P2FSendDailyState.class)

public class P2FSendDailyStateHandler extends Handler<P2FSendDailyState> {

    private static final Logger log = LogManager.getLogger(P2FSendDailyStateHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, P2FSendDailyState message) {
        try {
            long start = TimeUtils.Time();

            Manager.dailyActiveManager.deal().P2FSendDailyState(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FSendDailyStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}