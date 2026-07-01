package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqSplitSoulArmorBall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //分解魂印
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSplitSoulArmorBall.MsgID.eMsgID_VALUE, clazz = ReqSplitSoulArmorBall.class)

public class ReqSplitSoulArmorBallHandler extends Handler<ReqSplitSoulArmorBall> {

    static final Logger log = LogManager.getLogger(ReqSplitSoulArmorBallHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSplitSoulArmorBall messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqSplitSoulArmorBall(mess.getExecutor(), messInfo.getBallIdsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSplitSoulArmorBallHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
