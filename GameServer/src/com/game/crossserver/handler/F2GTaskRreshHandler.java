package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GTaskRresh;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服战场任务自动刷新   ---没有发送只有监听
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GTaskRresh.MsgID.eMsgID_VALUE, clazz = F2GTaskRresh.class)

public class F2GTaskRreshHandler extends Handler<F2GTaskRresh> {

    static final Logger log = LogManager.getLogger(F2GTaskRreshHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GTaskRresh message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GTaskFresh(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
