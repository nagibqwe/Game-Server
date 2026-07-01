package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireMatch;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireMatch.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireMatch.class)

public class G2PWorldBonfireMatchHandler extends Handler<G2PWorldBonfireMatch> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldBonfireManager.manager().onWorldBonfireMatch(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
