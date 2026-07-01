package com.game.worldanswer.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.G2PReqLeaveOutAnswer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服请求离开答题界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqLeaveOutAnswer.MsgID.eMsgID_VALUE, clazz = G2PReqLeaveOutAnswer.class)

public class G2PReqLeaveOutAnswerHandler extends Handler<G2PReqLeaveOutAnswer> {

    static final Logger log = LogManager.getLogger(G2PReqLeaveOutAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqLeaveOutAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldAnswerManager.getIWorldAnswer().onG2PReqPlayerLeaveAnswer(context,messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqLeaveOutAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
