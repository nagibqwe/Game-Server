package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqCoupleEscortOver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求护送结束
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCoupleEscortOver.MsgID.eMsgID_VALUE, clazz = ReqCoupleEscortOver.class)

public class ReqCoupleEscortOverHandler extends Handler<ReqCoupleEscortOver> {

    static final Logger log = LogManager.getLogger(ReqCoupleEscortOverHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCoupleEscortOver messInfo) {
        try {
            long start = TimeUtils.Time();


            Manager.couplefightManager.getCoupleEscort().onReqCoupleEscortOver((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCoupleEscortOverHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
