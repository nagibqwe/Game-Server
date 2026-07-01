package com.game.npc.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.npcMessage.ReqClickNpc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //点击npc
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqClickNpc.MsgID.eMsgID_VALUE, clazz = ReqClickNpc.class)

public class ReqClickNpcHandler extends Handler<ReqClickNpc> {

    static final Logger log = LogManager.getLogger(ReqClickNpcHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqClickNpc messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqClickNpcHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
