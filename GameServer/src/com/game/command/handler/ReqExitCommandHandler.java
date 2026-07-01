package com.game.command.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.ReqExitCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求退出指挥状态
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqExitCommand.MsgID.eMsgID_VALUE, clazz = ReqExitCommand.class)

public class ReqExitCommandHandler extends Handler<ReqExitCommand> {

    static final Logger log = LogManager.getLogger(ReqExitCommandHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqExitCommand message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.commandManager.deal().onReqExitCommand(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExitCommandHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
