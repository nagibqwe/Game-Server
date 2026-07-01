package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqCloudBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求云购
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCloudBuy.MsgID.eMsgID_VALUE, clazz = ReqCloudBuy.class)

public class ReqCloudBuyHandler extends Handler<ReqCloudBuy> {

    static final Logger log = LogManager.getLogger(ReqCloudBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCloudBuy message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCloudBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
