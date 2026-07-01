package com.game.soul.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulMessage.ReqHuntSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //狩魂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHuntSoul.MsgID.eMsgID_VALUE, clazz = ReqHuntSoul.class)

public class ReqHuntSoulHandler extends Handler<ReqHuntSoul> {

    static final Logger log = LogManager.getLogger(ReqHuntSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHuntSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHuntSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
