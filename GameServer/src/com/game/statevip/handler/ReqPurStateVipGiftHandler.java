package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqPurStateVipGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //购买礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPurStateVipGift.MsgID.eMsgID_VALUE, clazz = ReqPurStateVipGift.class)

public class ReqPurStateVipGiftHandler extends Handler<ReqPurStateVipGift> {

    static final Logger log = LogManager.getLogger(ReqPurStateVipGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPurStateVipGift messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPurStateVipGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
