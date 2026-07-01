package com.game.task.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqRefreshTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求刷新某个类型的任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRefreshTask.MsgID.eMsgID_VALUE, clazz = ReqRefreshTask.class)

public class ReqRefreshTaskHandler extends Handler<ReqRefreshTask> {

    static final Logger log = LogManager.getLogger(ReqRefreshTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRefreshTask messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefreshTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
