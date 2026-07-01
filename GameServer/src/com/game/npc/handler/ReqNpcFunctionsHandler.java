package com.game.npc.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.npcMessage.ReqNpcFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //查询npc的服务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNpcFunctions.MsgID.eMsgID_VALUE, clazz = ReqNpcFunctions.class)

public class ReqNpcFunctionsHandler extends Handler<ReqNpcFunctions> {

    static final Logger log = LogManager.getLogger(ReqNpcFunctionsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNpcFunctions messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNpcFunctionsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
