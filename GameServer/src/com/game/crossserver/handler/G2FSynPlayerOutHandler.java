package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FSynPlayerOut;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * makehandler  v1.6 for netty
 * 玩家离线同步
 */
@Message(id = CrossServerMessage.G2FSynPlayerOut.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FSynPlayerOut.class)
public class G2FSynPlayerOutHandler extends Handler<G2FSynPlayerOut> {

    private static final Logger log = LogManager.getLogger(G2FSynPlayerOutHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FSynPlayerOut message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossServerManager.getCrossServer().onG2FSynPlayerOut(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FSynPlayerOutHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}