package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqDelStateVipGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //删除当前礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDelStateVipGift.MsgID.eMsgID_VALUE, clazz = ReqDelStateVipGift.class)

public class ReqDelStateVipGiftHandler extends Handler<ReqDelStateVipGift> {

    static final Logger log = LogManager.getLogger(ReqDelStateVipGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDelStateVipGift messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDelStateVipGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
