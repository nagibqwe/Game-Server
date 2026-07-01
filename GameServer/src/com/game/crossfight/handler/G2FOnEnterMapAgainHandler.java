package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FOnEnterMapAgain;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 * 断线重连
 */
@Message(id = G2FOnEnterMapAgain.MsgID.eMsgID_VALUE, clazz = G2FOnEnterMapAgain.class)
public class G2FOnEnterMapAgainHandler extends Handler<G2FOnEnterMapAgain> {

    private static final Logger log = LogManager.getLogger(G2FOnEnterMapAgainHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FOnEnterMapAgain message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.GetCrossChangeMap().OnEnterMapAgain(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FOnEnterMapAgainHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}