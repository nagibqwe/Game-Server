package com.game.soul.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulMessage.ReqOneKeyHuntSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一键狩魂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOneKeyHuntSoul.MsgID.eMsgID_VALUE, clazz = ReqOneKeyHuntSoul.class)

public class ReqOneKeyHuntSoulHandler extends Handler<ReqOneKeyHuntSoul> {

    static final Logger log = LogManager.getLogger(ReqOneKeyHuntSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOneKeyHuntSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOneKeyHuntSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
