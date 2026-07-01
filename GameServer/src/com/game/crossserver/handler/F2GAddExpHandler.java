package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GAddExp;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服返回游戏服增加经验
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GAddExp.MsgID.eMsgID_VALUE, clazz = F2GAddExp.class)

public class F2GAddExpHandler extends Handler<F2GAddExp> {

    static final Logger log = LogManager.getLogger(F2GAddExpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GAddExp message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GAddExp(context,message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GAddExpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
