package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqBuyTimeLimitGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买限时礼包
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBuyTimeLimitGift.MsgID.eMsgID_VALUE, clazz = ReqBuyTimeLimitGift.class)

public class ReqBuyTimeLimitGiftHandler extends Handler<ReqBuyTimeLimitGift> {

    static final Logger log = LogManager.getLogger(ReqBuyTimeLimitGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBuyTimeLimitGift message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyTimeLimitGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
