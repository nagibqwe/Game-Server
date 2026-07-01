package com.game.heart.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage.ReqReconnect;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //断线重联
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqReconnect.MsgID.eMsgID_VALUE, clazz = ReqReconnect.class)

public class ReqReconnectHandler extends Handler<ReqReconnect> {

    static final Logger log = LogManager.getLogger(ReqReconnectHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqReconnect message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.heartManager.reconnect(context, message.getPlayerId(), message.getSign());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
