package com.game.questionnaire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.ReqSubmitAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //提交问卷
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSubmitAnswer.MsgID.eMsgID_VALUE, clazz = ReqSubmitAnswer.class)

public class ReqSubmitAnswerHandler extends Handler<ReqSubmitAnswer> {

    static final Logger log = LogManager.getLogger(ReqSubmitAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSubmitAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.questionnaireManager.deal().onReqSubmitAnswer(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSubmitAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
