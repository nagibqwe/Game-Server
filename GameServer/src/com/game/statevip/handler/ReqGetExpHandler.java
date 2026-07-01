package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqGetExp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Clinet -> Server 领取经验
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetExp.MsgID.eMsgID_VALUE, clazz = ReqGetExp.class)

public class ReqGetExpHandler extends Handler<ReqGetExp> {

    static final Logger log = LogManager.getLogger(ReqGetExpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetExp messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetExpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
