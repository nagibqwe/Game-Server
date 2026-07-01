package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqClosePopWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求设置当日不再弹出活动窗口
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqClosePopWindow.MsgID.eMsgID_VALUE, clazz = ReqClosePopWindow.class)

public class ReqClosePopWindowHandler extends Handler<ReqClosePopWindow> {

    static final Logger log = LogManager.getLogger(ReqClosePopWindowHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqClosePopWindow message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqClosePopWindowHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
