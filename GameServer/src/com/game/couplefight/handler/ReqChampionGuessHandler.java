package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionGuess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求参与竞猜
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionGuess.MsgID.eMsgID_VALUE, clazz = ReqChampionGuess.class)

public class ReqChampionGuessHandler extends Handler<ReqChampionGuess> {

    static final Logger log = LogManager.getLogger(ReqChampionGuessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionGuess messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqChampionGuess((Player) mess.getExecutor(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionGuessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
