package com.game.alienboss.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.P2FEnterCrossAlienGem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入须弥宝库
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FEnterCrossAlienGem.MsgID.eMsgID_VALUE, clazz = P2FEnterCrossAlienGem.class)

public class P2FEnterCrossAlienGemHandler extends Handler<P2FEnterCrossAlienGem> {

    static final Logger log = LogManager.getLogger(P2FEnterCrossAlienGemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FEnterCrossAlienGem messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FEnterCrossAlienGemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
