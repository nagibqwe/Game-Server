package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.P2GResCrossHorseBossRefreshTip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //boss刷新提前一分钟提示
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GResCrossHorseBossRefreshTip.MsgID.eMsgID_VALUE, clazz = P2GResCrossHorseBossRefreshTip.class)

public class P2GResCrossHorseBossRefreshTipHandler extends Handler<P2GResCrossHorseBossRefreshTip> {

    static final Logger log = LogManager.getLogger(P2GResCrossHorseBossRefreshTipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GResCrossHorseBossRefreshTip message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossHorseBossManager.deal().onP2GResCrossHorseBossRefreshTip(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResCrossHorseBossRefreshTipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
