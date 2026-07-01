package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionGuessWatching;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求观战
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionGuessWatching.MsgID.eMsgID_VALUE, clazz = ReqChampionGuessWatching.class)

public class ReqChampionGuessWatchingHandler extends Handler<ReqChampionGuessWatching> {

    static final Logger log = LogManager.getLogger(ReqChampionGuessWatchingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionGuessWatching messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player)mess.getExecutor(), 36, messInfo.getType(), messInfo.getFightId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionGuessWatchingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
