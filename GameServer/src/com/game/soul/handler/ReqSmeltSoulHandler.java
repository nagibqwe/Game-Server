package com.game.soul.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulMessage.ReqSmeltSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 吞噬战魂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSmeltSoul.MsgID.eMsgID_VALUE, clazz = ReqSmeltSoul.class)

public class ReqSmeltSoulHandler extends Handler<ReqSmeltSoul> {

    static final Logger log = LogManager.getLogger(ReqSmeltSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSmeltSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSmeltSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
