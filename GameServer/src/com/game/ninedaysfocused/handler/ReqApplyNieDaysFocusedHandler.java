package com.game.ninedaysfocused.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage.ReqApplyNieDaysFocused;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求报名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqApplyNieDaysFocused.MsgID.eMsgID_VALUE, clazz = ReqApplyNieDaysFocused.class)

public class ReqApplyNieDaysFocusedHandler extends Handler<ReqApplyNieDaysFocused> {

    static final Logger log = LogManager.getLogger(ReqApplyNieDaysFocusedHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqApplyNieDaysFocused messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqApplyNieDaysFocusedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
