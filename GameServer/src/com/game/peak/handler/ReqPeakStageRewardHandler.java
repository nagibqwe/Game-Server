package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqPeakStageReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取段位奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPeakStageReward.MsgID.eMsgID_VALUE, clazz = ReqPeakStageReward.class)

public class ReqPeakStageRewardHandler extends Handler<ReqPeakStageReward> {

    static final Logger log = LogManager.getLogger(ReqPeakStageRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPeakStageReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqPeakStageReward(mess.getExecutor(), messInfo.getStageId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPeakStageRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
