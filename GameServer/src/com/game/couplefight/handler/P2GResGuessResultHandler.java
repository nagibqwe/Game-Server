package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GResGuessResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-竞猜奖励发放
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResGuessResult.MsgID.eMsgID_VALUE, clazz = P2GResGuessResult.class)

public class P2GResGuessResultHandler extends Handler<P2GResGuessResult> {

    static final Logger log = LogManager.getLogger(P2GResGuessResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResGuessResult messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GResGuessResult(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResGuessResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
