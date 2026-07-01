package com.game.questionnaire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.G2PSubmitAnswer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服-->公共服  提交问卷
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PSubmitAnswer.MsgID.eMsgID_VALUE, clazz = G2PSubmitAnswer.class)

public class G2PSubmitAnswerHandler extends Handler<G2PSubmitAnswer> {

    static final Logger log = LogManager.getLogger(G2PSubmitAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PSubmitAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.questionnaireManager.deal().onG2PSubmitAnswer(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PSubmitAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
