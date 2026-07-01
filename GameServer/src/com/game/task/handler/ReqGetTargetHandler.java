package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqGetTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取目标阶段奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetTarget.MsgID.eMsgID_VALUE, clazz = ReqGetTarget.class)

public class ReqGetTargetHandler extends Handler<ReqGetTarget> {

    static final Logger log = LogManager.getLogger(ReqGetTargetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetTarget messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.deal().onReqGetTarget(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetTargetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
