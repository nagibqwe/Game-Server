package com.game.command.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.ReqJoinCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求加入指挥状态
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqJoinCommand.MsgID.eMsgID_VALUE, clazz = ReqJoinCommand.class)

public class ReqJoinCommandHandler extends Handler<ReqJoinCommand> {

    static final Logger log = LogManager.getLogger(ReqJoinCommandHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqJoinCommand message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.commandManager.deal().onReqJoinCommand(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqJoinCommandHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
