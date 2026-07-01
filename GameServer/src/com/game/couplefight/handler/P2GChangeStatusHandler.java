package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GChangeStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-活动进程改变
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GChangeStatus.MsgID.eMsgID_VALUE, clazz = P2GChangeStatus.class)

public class P2GChangeStatusHandler extends Handler<P2GChangeStatus> {

    static final Logger log = LogManager.getLogger(P2GChangeStatusHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GChangeStatus messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GChangeStatus(messInfo.getStatus());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GChangeStatusHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
