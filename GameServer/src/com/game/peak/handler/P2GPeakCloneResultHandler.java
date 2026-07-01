package com.game.peak.handler;

import com.game.manager.Manager;
import com.game.peak.timer.CrossPeakCloneEvent;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.P2GPeakCloneResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知挑战结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GPeakCloneResult.MsgID.eMsgID_VALUE, clazz = P2GPeakCloneResult.class)

public class P2GPeakCloneResultHandler extends Handler<P2GPeakCloneResult> {

    static final Logger log = LogManager.getLogger(P2GPeakCloneResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GPeakCloneResult messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.addCommand(new CrossPeakCloneEvent(messInfo.getRoleId(), messInfo.getIsWin(), messInfo.getExp()));

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GPeakCloneResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
