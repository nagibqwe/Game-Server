package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqEnterCoupleEscort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入护送
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterCoupleEscort.MsgID.eMsgID_VALUE, clazz = ReqEnterCoupleEscort.class)

public class ReqEnterCoupleEscortHandler extends Handler<ReqEnterCoupleEscort> {

    static final Logger log = LogManager.getLogger(ReqEnterCoupleEscortHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEnterCoupleEscort messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getCoupleEscort().onReqEnterCoupleEscort((Player) mess.getExecutor(), messInfo.getType());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterCoupleEscortHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
