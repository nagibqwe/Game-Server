package com.game.heart.handler;

import com.game.player.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage.ReqReallyHeart;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //这个才是真实的心跳包，每10秒发送下，3分钟没收到服务器踢下线
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqReallyHeart.MsgID.eMsgID_VALUE, clazz = ReqReallyHeart.class)

public class ReqReallyHeartHandler extends Handler<ReqReallyHeart> {

    static final Logger log = LogManager.getLogger(ReqReallyHeartHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqReallyHeart message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            context.channel().attr(SessionAttribute.HeartSendTime).set(start);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
