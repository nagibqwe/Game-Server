package com.game.openserverac.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqV4RebateCompleteTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqV4RebateCompleteTask.MsgID.eMsgID_VALUE, clazz = ReqV4RebateCompleteTask.class)

public class ReqV4RebateCompleteTaskHandler extends Handler<ReqV4RebateCompleteTask> {

    static final Logger log = LogManager.getLogger(ReqV4RebateCompleteTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqV4RebateCompleteTask messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.openServerAcManager.v4Rebate().onReqV4RebateCompleteTask(mess.getExecutor(),messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqV4RebateCompleteTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
