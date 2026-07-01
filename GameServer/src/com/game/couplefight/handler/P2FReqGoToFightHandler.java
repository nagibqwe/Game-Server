package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2FReqGoToFight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到战斗服-切换到战斗地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FReqGoToFight.MsgID.eMsgID_VALUE, clazz = P2FReqGoToFight.class)

public class P2FReqGoToFightHandler extends Handler<P2FReqGoToFight> {

    static final Logger log = LogManager.getLogger(P2FReqGoToFightHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FReqGoToFight messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.couplefightManager.getScript().reqGoToFight(messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FReqGoToFightHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
