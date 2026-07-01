package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqRemainTimeActivity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开倒计时活动面板
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRemainTimeActivity.MsgID.eMsgID_VALUE, clazz = ReqRemainTimeActivity.class)

public class ReqRemainTimeActivityHandler extends Handler<ReqRemainTimeActivity> {

    static final Logger log = LogManager.getLogger(ReqRemainTimeActivityHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRemainTimeActivity message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRemainTimeActivityHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
