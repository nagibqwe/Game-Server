package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqPeakTimesReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取场次奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPeakTimesReward.MsgID.eMsgID_VALUE, clazz = ReqPeakTimesReward.class)

public class ReqPeakTimesRewardHandler extends Handler<ReqPeakTimesReward> {

    static final Logger log = LogManager.getLogger(ReqPeakTimesRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPeakTimesReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqPeakTimesReward(mess.getExecutor(), messInfo.getTimes());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPeakTimesRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
