package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqGetReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetReward.MsgID.eMsgID_VALUE, clazz = ReqGetReward.class)

public class ReqGetRewardHandler extends Handler<ReqGetReward> {

    static final Logger log = LogManager.getLogger(ReqGetRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetReward messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
