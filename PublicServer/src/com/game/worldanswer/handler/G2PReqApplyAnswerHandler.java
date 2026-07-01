package com.game.worldanswer.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.G2PReqApplyAnswer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服报名答题
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqApplyAnswer.MsgID.eMsgID_VALUE, clazz = G2PReqApplyAnswer.class)

public class G2PReqApplyAnswerHandler extends Handler<G2PReqApplyAnswer> {

    static final Logger log = LogManager.getLogger(G2PReqApplyAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqApplyAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldAnswerManager.getIWorldAnswer().onG2PReqApplyAnswer(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqApplyAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
