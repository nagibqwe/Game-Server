package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FReqCrossDropItemString;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.6 for netty 战斗掉落物品的增加反馈
 */
@Message(id = CrossServerMessage.G2FReqCrossDropItemString.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FReqCrossDropItemString.class)
public class G2FReqCrossDropItemStringHandler extends Handler<G2FReqCrossDropItemString> {

    private static final Logger log = LogManager.getLogger(G2FReqCrossDropItemStringHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FReqCrossDropItemString message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnG2FReqCrossDropItemString(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqCrossDropItemStringHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
