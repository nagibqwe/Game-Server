package com.game.worldanswer.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.G2PReqAnswerResult;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服发送答题答案
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqAnswerResult.MsgID.eMsgID_VALUE, clazz = G2PReqAnswerResult.class)

public class G2PReqAnswerResultHandler extends Handler<G2PReqAnswerResult> {

    static final Logger log = LogManager.getLogger(G2PReqAnswerResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqAnswerResult messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldAnswerManager.getIWorldAnswer().onG2PReqPlayerAnswerResult(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqAnswerResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
