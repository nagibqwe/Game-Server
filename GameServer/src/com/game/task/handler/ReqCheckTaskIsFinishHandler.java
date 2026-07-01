package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqCheckTaskIsFinish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求查询任务是否已经完成
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCheckTaskIsFinish.MsgID.eMsgID_VALUE, clazz = ReqCheckTaskIsFinish.class)

public class ReqCheckTaskIsFinishHandler extends Handler<ReqCheckTaskIsFinish> {

    static final Logger log = LogManager.getLogger(ReqCheckTaskIsFinishHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCheckTaskIsFinish messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.deal().OnReqCheckTaskIsFinish(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCheckTaskIsFinishHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
