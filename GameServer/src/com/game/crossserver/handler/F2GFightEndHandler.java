package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GFightEnd;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //副本玩家战斗结束，离开副本
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GFightEnd.MsgID.eMsgID_VALUE, clazz = F2GFightEnd.class)

public class F2GFightEndHandler extends Handler<F2GFightEnd> {

    static final Logger log = LogManager.getLogger(F2GFightEndHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GFightEnd message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GFightEnd(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GFightEndHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
