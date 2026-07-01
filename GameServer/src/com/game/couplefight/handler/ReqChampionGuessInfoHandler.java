package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionGuessInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求竞猜界面数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionGuessInfo.MsgID.eMsgID_VALUE, clazz = ReqChampionGuessInfo.class)

public class ReqChampionGuessInfoHandler extends Handler<ReqChampionGuessInfo> {

    static final Logger log = LogManager.getLogger(ReqChampionGuessInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionGuessInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player) mess.getExecutor(), 31, messInfo.getType(), messInfo.getFightId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionGuessInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
