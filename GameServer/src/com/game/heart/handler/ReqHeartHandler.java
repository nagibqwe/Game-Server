package com.game.heart.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage.ReqHeart;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //客户端收到ResHear消息后10秒返回，用于加速检查
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqHeart.MsgID.eMsgID_VALUE, clazz = ReqHeart.class)

public class ReqHeartHandler extends Handler<ReqHeart> {

    static final Logger log = LogManager.getLogger(ReqHeartHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqHeart message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.heartManager.deal().OnHeartReceive(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
