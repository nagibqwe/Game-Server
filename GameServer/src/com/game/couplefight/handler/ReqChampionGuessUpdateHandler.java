package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionGuessUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求竞猜支持率更新
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionGuessUpdate.MsgID.eMsgID_VALUE, clazz = ReqChampionGuessUpdate.class)

public class ReqChampionGuessUpdateHandler extends Handler<ReqChampionGuessUpdate> {

    static final Logger log = LogManager.getLogger(ReqChampionGuessUpdateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionGuessUpdate messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player) mess.getExecutor(), 32, messInfo.getType(), messInfo.getFightId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionGuessUpdateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
