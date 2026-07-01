package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqCancelPeakMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //取消匹配 巅峰竞技
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCancelPeakMatch.MsgID.eMsgID_VALUE, clazz = ReqCancelPeakMatch.class)

public class ReqCancelPeakMatchHandler extends Handler<ReqCancelPeakMatch> {

    static final Logger log = LogManager.getLogger(ReqCancelPeakMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCancelPeakMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqCancelPeakMatch(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCancelPeakMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
