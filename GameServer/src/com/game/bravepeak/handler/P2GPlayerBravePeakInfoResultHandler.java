package com.game.bravepeak.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BravePeakMessage.P2GPlayerBravePeakInfoResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Public -> Game 请求获取玩家勇者巅峰信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GPlayerBravePeakInfoResult.MsgID.eMsgID_VALUE, clazz = P2GPlayerBravePeakInfoResult.class)

public class P2GPlayerBravePeakInfoResultHandler extends Handler<P2GPlayerBravePeakInfoResult> {

    static final Logger log = LogManager.getLogger(P2GPlayerBravePeakInfoResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GPlayerBravePeakInfoResult message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GPlayerBravePeakInfoResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
