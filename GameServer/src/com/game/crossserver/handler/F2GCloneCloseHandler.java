package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GCloneClose;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //副本关闭，防止因为掉信息而没有出副本
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GCloneClose.MsgID.eMsgID_VALUE, clazz = F2GCloneClose.class)

public class F2GCloneCloseHandler extends Handler<F2GCloneClose> {

    static final Logger log = LogManager.getLogger(F2GCloneCloseHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GCloneClose message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GCloneClose(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GCloneCloseHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
