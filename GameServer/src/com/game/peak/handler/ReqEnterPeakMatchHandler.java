package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqEnterPeakMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //匹配 巅峰竞技
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterPeakMatch.MsgID.eMsgID_VALUE, clazz = ReqEnterPeakMatch.class)

public class ReqEnterPeakMatchHandler extends Handler<ReqEnterPeakMatch> {

    static final Logger log = LogManager.getLogger(ReqEnterPeakMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEnterPeakMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqEnterPeakMatch(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterPeakMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
