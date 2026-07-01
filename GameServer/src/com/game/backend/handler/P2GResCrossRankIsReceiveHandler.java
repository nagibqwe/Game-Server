package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.P2GResCrossRankIsReceive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GResCrossRankIsReceive.MsgID.eMsgID_VALUE, clazz = P2GResCrossRankIsReceive.class)

public class P2GResCrossRankIsReceiveHandler extends Handler<P2GResCrossRankIsReceive> {

    static final Logger log = LogManager.getLogger(P2GResCrossRankIsReceiveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GResCrossRankIsReceive message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResCrossRankIsReceiveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
