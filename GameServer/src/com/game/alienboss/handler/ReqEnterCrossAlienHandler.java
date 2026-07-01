package com.game.alienboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.ReqEnterCrossAlien;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入混沌虚空
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterCrossAlien.MsgID.eMsgID_VALUE, clazz = ReqEnterCrossAlien.class)

public class ReqEnterCrossAlienHandler extends Handler<ReqEnterCrossAlien> {

    static final Logger log = LogManager.getLogger(ReqEnterCrossAlienHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEnterCrossAlien message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqEnterCrossAlienHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterCrossAlienHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
