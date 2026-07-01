package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireEnter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服篝火
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireEnter.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireEnter.class)

public class G2PWorldBonfireEnterHandler extends Handler<G2PWorldBonfireEnter> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireEnterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireEnter messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldBonfireManager.manager().onWorldBonfireEnter(context, messInfo.getRoleID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireEnterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
