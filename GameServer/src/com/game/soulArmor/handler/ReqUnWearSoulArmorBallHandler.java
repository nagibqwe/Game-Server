package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqUnWearSoulArmorBall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //卸下魂印
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUnWearSoulArmorBall.MsgID.eMsgID_VALUE, clazz = ReqUnWearSoulArmorBall.class)

public class ReqUnWearSoulArmorBallHandler extends Handler<ReqUnWearSoulArmorBall> {

    static final Logger log = LogManager.getLogger(ReqUnWearSoulArmorBallHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUnWearSoulArmorBall messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqUnWearSoulArmorBall(mess.getExecutor(), messInfo.getSlotId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUnWearSoulArmorBallHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
