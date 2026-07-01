package com.game.player.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqCourageList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //勇气值的每天可获得列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCourageList.MsgID.eMsgID_VALUE, clazz = ReqCourageList.class)

public class ReqCourageListHandler extends Handler<ReqCourageList> {

    static final Logger log = LogManager.getLogger(ReqCourageListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCourageList messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCourageListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
