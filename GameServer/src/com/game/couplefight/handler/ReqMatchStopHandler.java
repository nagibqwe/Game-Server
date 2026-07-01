package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqMatchStop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求停止匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMatchStop.MsgID.eMsgID_VALUE, clazz = ReqMatchStop.class)

public class ReqMatchStopHandler extends Handler<ReqMatchStop> {

    static final Logger log = LogManager.getLogger(ReqMatchStopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMatchStop messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().matchStop((Player)mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMatchStopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
