package com.game.backpack.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqDelItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //删除物品消息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDelItem.MsgID.eMsgID_VALUE, clazz = ReqDelItem.class)

public class ReqDelItemHandler extends Handler<ReqDelItem> {

    static final Logger log = LogManager.getLogger(ReqDelItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDelItem message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDelItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
