package com.game.questionnaire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.P2GGetRewardState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GGetRewardState.MsgID.eMsgID_VALUE, clazz = P2GGetRewardState.class)

public class P2GGetRewardStateHandler extends Handler<P2GGetRewardState> {

    static final Logger log = LogManager.getLogger(P2GGetRewardStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GGetRewardState messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.questionnaireManager.deal().onP2GGetRewardState(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GGetRewardStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
