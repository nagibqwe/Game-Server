package com.game.command.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.ReqFocusTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求集火目标
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFocusTarget.MsgID.eMsgID_VALUE, clazz = ReqFocusTarget.class)

public class ReqFocusTargetHandler extends Handler<ReqFocusTarget> {

    static final Logger log = LogManager.getLogger(ReqFocusTargetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFocusTarget message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.commandManager.deal().onReqFocusTarget(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFocusTargetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
