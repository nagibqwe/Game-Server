package com.game.ninedaysfocused.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage.F2GSynchrotask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步任务数据到游戏服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2GSynchrotask.MsgID.eMsgID_VALUE, clazz = F2GSynchrotask.class)

public class F2GSynchrotaskHandler extends Handler<F2GSynchrotask> {

    static final Logger log = LogManager.getLogger(F2GSynchrotaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2GSynchrotask messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GSynchrotaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
