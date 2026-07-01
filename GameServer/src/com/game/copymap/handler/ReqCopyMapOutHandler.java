package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCopyMapOut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //放弃离开副本
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCopyMapOut.MsgID.eMsgID_VALUE, clazz = ReqCopyMapOut.class)

public class ReqCopyMapOutHandler extends Handler<ReqCopyMapOut> {

    static final Logger log = LogManager.getLogger(ReqCopyMapOutHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCopyMapOut message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.copyMapManager.manager().onReqCopyMapOut(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCopyMapOutHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
