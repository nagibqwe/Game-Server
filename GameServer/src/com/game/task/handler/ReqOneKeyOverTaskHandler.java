package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqOneKeyOverTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一键完成当前指定的任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOneKeyOverTask.MsgID.eMsgID_VALUE, clazz = ReqOneKeyOverTask.class)

public class ReqOneKeyOverTaskHandler extends Handler<ReqOneKeyOverTask> {

    static final Logger log = LogManager.getLogger(ReqOneKeyOverTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOneKeyOverTask messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            //策划的新需求不能领取双倍
            Manager.taskManager.deal().OnReqOneKeyOverTask(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOneKeyOverTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
