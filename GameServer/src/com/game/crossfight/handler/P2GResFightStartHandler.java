package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.P2GResFightStart;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知战场所有人进入战斗服
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GResFightStart.MsgID.eMsgID_VALUE, clazz = P2GResFightStart.class)

public class P2GResFightStartHandler extends Handler<P2GResFightStart> {

    static final Logger log = LogManager.getLogger(P2GResFightStartHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GResFightStart message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnP2GResFightStart(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResFightStartHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
