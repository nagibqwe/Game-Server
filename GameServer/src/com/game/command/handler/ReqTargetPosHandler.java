package com.game.command.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.ReqTargetPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求目标位置
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqTargetPos.MsgID.eMsgID_VALUE, clazz = ReqTargetPos.class)

public class ReqTargetPosHandler extends Handler<ReqTargetPos> {

    static final Logger log = LogManager.getLogger(ReqTargetPosHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqTargetPos message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.commandManager.deal().onReqTargetPos(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTargetPosHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
