package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.F2PPeakCloneResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知挑战结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PPeakCloneResult.MsgID.eMsgID_VALUE, clazz = F2PPeakCloneResult.class)

public class F2PPeakCloneResultHandler extends Handler<F2PPeakCloneResult> {

    static final Logger log = LogManager.getLogger(F2PPeakCloneResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PPeakCloneResult messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().F2PPeakCloneResult(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PPeakCloneResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
