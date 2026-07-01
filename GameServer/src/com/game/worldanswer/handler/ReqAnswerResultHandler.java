package com.game.worldanswer.handler;

import com.game.player.structs.Player;
import com.game.worldanswer.manager.WorldAnswerManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.ReqAnswerResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求答题
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAnswerResult.MsgID.eMsgID_VALUE, clazz = ReqAnswerResult.class)

public class ReqAnswerResultHandler extends Handler<ReqAnswerResult> {

    static final Logger log = LogManager.getLogger(ReqAnswerResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAnswerResult messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            WorldAnswerManager.getInstance().getIWorldAnswer().playerAnswerResult(player,messInfo.getResultIndex());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAnswerResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
