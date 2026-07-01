package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqTaskFinish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //交付任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTaskFinish.MsgID.eMsgID_VALUE, clazz = ReqTaskFinish.class)

public class ReqTaskFinishHandler extends Handler<ReqTaskFinish> {

    static final Logger log = LogManager.getLogger(ReqTaskFinishHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTaskFinish messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.deal().OnReqTaskFinish(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTaskFinishHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
