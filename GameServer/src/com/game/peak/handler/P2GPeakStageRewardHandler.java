package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.P2GPeakStageReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知游戏服发放段位奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GPeakStageReward.MsgID.eMsgID_VALUE, clazz = P2GPeakStageReward.class)

public class P2GPeakStageRewardHandler extends Handler<P2GPeakStageReward> {

    static final Logger log = LogManager.getLogger(P2GPeakStageRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GPeakStageReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().P2GPeakStageReward(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GPeakStageRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
