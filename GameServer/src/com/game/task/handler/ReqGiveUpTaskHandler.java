package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqGiveUpTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //放弃任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGiveUpTask.MsgID.eMsgID_VALUE, clazz = ReqGiveUpTask.class)

public class ReqGiveUpTaskHandler extends Handler<ReqGiveUpTask> {

    static final Logger log = LogManager.getLogger(ReqGiveUpTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGiveUpTask messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.guild().onReqGiveUpTask(player,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGiveUpTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
