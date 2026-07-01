package com.game.questionnaire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.P2GOpenState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服返回  开启状态
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GOpenState.MsgID.eMsgID_VALUE, clazz = P2GOpenState.class)

public class P2GOpenStateHandler extends Handler<P2GOpenState> {

    static final Logger log = LogManager.getLogger(P2GOpenStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GOpenState messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.questionnaireManager.deal().onP2GOpenState(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GOpenStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
