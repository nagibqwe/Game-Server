package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqChangeTaskState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求任务状态变更，主要用于需要在某个具体坐标做特殊处理之后才能完成的任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeTaskState.MsgID.eMsgID_VALUE, clazz = ReqChangeTaskState.class)

public class ReqChangeTaskStateHandler extends Handler<ReqChangeTaskState> {

    static final Logger log = LogManager.getLogger(ReqChangeTaskStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeTaskState messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.deal().OnReqChangeTaskState(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeTaskStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
