package com.game.soul.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulMessage.ReqActivateHunter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //激活狩魂师
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActivateHunter.MsgID.eMsgID_VALUE, clazz = ReqActivateHunter.class)

public class ReqActivateHunterHandler extends Handler<ReqActivateHunter> {

    static final Logger log = LogManager.getLogger(ReqActivateHunterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActivateHunter messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivateHunterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
