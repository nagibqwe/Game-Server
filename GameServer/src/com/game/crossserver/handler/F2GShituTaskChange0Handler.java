package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GShituTaskChange0;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //师徒任务跨服变更通知
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GShituTaskChange0.MsgID.eMsgID_VALUE, clazz = F2GShituTaskChange0.class)

public class F2GShituTaskChange0Handler extends Handler<F2GShituTaskChange0> {

    static final Logger log = LogManager.getLogger(F2GShituTaskChange0Handler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GShituTaskChange0 message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().onF2GShituTaskChange(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
