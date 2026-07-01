package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqWearSoulArmorBall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //穿戴魂印
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWearSoulArmorBall.MsgID.eMsgID_VALUE, clazz = ReqWearSoulArmorBall.class)

public class ReqWearSoulArmorBallHandler extends Handler<ReqWearSoulArmorBall> {

    static final Logger log = LogManager.getLogger(ReqWearSoulArmorBallHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWearSoulArmorBall messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqWearSoulArmorBall(mess.getExecutor(), messInfo.getSlotId(), messInfo.getBallId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWearSoulArmorBallHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
