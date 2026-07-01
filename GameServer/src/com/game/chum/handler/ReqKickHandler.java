package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqKick;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 踢人
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqKick.MsgID.eMsgID_VALUE, clazz = ReqKick.class)

public class ReqKickHandler extends Handler<ReqKick> {

    static final Logger log = LogManager.getLogger(ReqKickHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqKick message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqKick(player, message.getTID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqKickHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
