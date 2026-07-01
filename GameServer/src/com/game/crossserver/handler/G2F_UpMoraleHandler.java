package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2F_UpMorale;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.6 for netty 鼓舞
 */
@Message(id = CrossServerMessage.G2F_UpMorale.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2F_UpMorale.class)
public class G2F_UpMoraleHandler extends Handler<G2F_UpMorale> {

    private static final Logger log = LogManager.getLogger(G2F_UpMoraleHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2F_UpMorale message) {

        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().G2F_UpMorale(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2F_UpMoraleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
