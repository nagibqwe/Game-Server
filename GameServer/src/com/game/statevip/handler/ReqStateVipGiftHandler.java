package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqStateVipGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置当前礼包(下线发送)
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqStateVipGift.MsgID.eMsgID_VALUE, clazz = ReqStateVipGift.class)

public class ReqStateVipGiftHandler extends Handler<ReqStateVipGift> {

    static final Logger log = LogManager.getLogger(ReqStateVipGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqStateVipGift messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqStateVipGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
