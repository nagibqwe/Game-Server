package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GResChampionGuess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-竞猜成功
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResChampionGuess.MsgID.eMsgID_VALUE, clazz = P2GResChampionGuess.class)

public class P2GResChampionGuessHandler extends Handler<P2GResChampionGuess> {

    static final Logger log = LogManager.getLogger(P2GResChampionGuessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResChampionGuess messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GResChampionGuess(messInfo.getRid());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResChampionGuessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
