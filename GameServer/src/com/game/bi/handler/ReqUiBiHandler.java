package com.game.bi.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BIMessage.ReqUiBi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqUiBi.MsgID.eMsgID_VALUE, clazz = ReqUiBi.class)

public class ReqUiBiHandler extends Handler<ReqUiBi> {

    static final Logger log = LogManager.getLogger(ReqUiBiHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUiBi message) {
        try {
            long start = TimeUtils.Time();

            Manager.biManager.getScript().onReqUiBi((Player) session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUiBiHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
