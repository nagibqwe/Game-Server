package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FGMdeal;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.6 for netty 玩家输入的GM命令执行
 */
@Message(id = CrossServerMessage.G2FGMdeal.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FGMdeal.class)
public class G2FGMdealHandler extends Handler<G2FGMdeal> {

    private static final Logger log = LogManager.getLogger(G2FGMdealHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FGMdeal message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.gmCommandManager.getGM().OnFightGM(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FGMdealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
