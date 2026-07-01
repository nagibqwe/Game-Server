package com.game.npc.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.npcMessage.ReqNpcFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求npc的功能服务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNpcFunction.MsgID.eMsgID_VALUE, clazz = ReqNpcFunction.class)

public class ReqNpcFunctionHandler extends Handler<ReqNpcFunction> {

    static final Logger log = LogManager.getLogger(ReqNpcFunctionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNpcFunction messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNpcFunctionHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
