package com.game.couplefight.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.F2PReqGroupPrepareMapOut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //战斗服到公共服-请求退出准备地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PReqGroupPrepareMapOut.MsgID.eMsgID_VALUE, clazz = F2PReqGroupPrepareMapOut.class)

public class F2PReqGroupPrepareMapOutHandler extends Handler<F2PReqGroupPrepareMapOut> {

    static final Logger log = LogManager.getLogger(F2PReqGroupPrepareMapOutHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PReqGroupPrepareMapOut messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PReqGroupPrepareMapOutHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
