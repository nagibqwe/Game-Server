package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.P2GCheckCrossInfoRes;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //public 返回game的跨服信息   ----已经没用了
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCheckCrossInfoRes.MsgID.eMsgID_VALUE, clazz = P2GCheckCrossInfoRes.class)

public class P2GCheckCrossInfoResHandler extends Handler<P2GCheckCrossInfoRes> {

    static final Logger log = LogManager.getLogger(P2GCheckCrossInfoResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCheckCrossInfoRes message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnP2GCheckCrossInfoRes(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GCheckCrossInfoResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
