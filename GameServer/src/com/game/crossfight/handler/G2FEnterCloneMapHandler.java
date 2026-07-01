package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FEnterCloneMap;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 * -------------------------------------------请求进入跨服副本
 */
@Message(id = G2FEnterCloneMap.MsgID.eMsgID_VALUE, clazz = G2FEnterCloneMap.class)

public class G2FEnterCloneMapHandler extends Handler<G2FEnterCloneMap> {

    private static final Logger log = LogManager.getLogger(G2FEnterCloneMapHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FEnterCloneMap message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.GetCrossChangeMap().OnG2FEnterCloneMap(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FEnterCloneMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}