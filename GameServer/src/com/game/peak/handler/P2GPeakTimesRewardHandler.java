package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.P2GPeakTimesReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知游戏服发放未领取场次奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GPeakTimesReward.MsgID.eMsgID_VALUE, clazz = P2GPeakTimesReward.class)

public class P2GPeakTimesRewardHandler extends Handler<P2GPeakTimesReward> {

    static final Logger log = LogManager.getLogger(P2GPeakTimesRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GPeakTimesReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().P2GPeakTimesReward(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GPeakTimesRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
