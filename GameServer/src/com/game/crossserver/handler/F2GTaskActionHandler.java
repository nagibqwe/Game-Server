package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GTaskAction;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知玩家的任务值变化
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GTaskAction.MsgID.eMsgID_VALUE, clazz = F2GTaskAction.class)

public class F2GTaskActionHandler extends Handler<F2GTaskAction> {

    static final Logger log = LogManager.getLogger(F2GTaskActionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GTaskAction message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GTaskAction(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
