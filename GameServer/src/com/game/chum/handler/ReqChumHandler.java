package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqChum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // request
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChum.MsgID.eMsgID_VALUE, clazz = ReqChum.class)

public class ReqChumHandler extends Handler<ReqChum> {

    static final Logger log = LogManager.getLogger(ReqChumHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChum message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqChum(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChumHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
