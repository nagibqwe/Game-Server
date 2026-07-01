package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.F2GEnterCloneMapRes;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服副本返回
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, clazz = F2GEnterCloneMapRes.class)

public class F2GEnterCloneMapResHandler extends Handler<F2GEnterCloneMapRes> {

    static final Logger log = LogManager.getLogger(F2GEnterCloneMapResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GEnterCloneMapRes message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.GetCrossChangeMap().OnF2GEnterCloneMapRes(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GEnterCloneMapResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
