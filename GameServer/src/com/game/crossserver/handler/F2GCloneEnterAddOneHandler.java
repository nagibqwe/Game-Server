package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GCloneEnterAddOne;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置玩家的进入次数+1
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GCloneEnterAddOne.MsgID.eMsgID_VALUE, clazz = F2GCloneEnterAddOne.class)

public class F2GCloneEnterAddOneHandler extends Handler<F2GCloneEnterAddOne> {

    static final Logger log = LogManager.getLogger(F2GCloneEnterAddOneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GCloneEnterAddOne message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnF2GCloneEnterAddOne(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GCloneEnterAddOneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
