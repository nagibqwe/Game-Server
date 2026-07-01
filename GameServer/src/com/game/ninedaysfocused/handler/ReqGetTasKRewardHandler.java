package com.game.ninedaysfocused.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage.ReqGetTasKReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取任务奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetTasKReward.MsgID.eMsgID_VALUE, clazz = ReqGetTasKReward.class)

public class ReqGetTasKRewardHandler extends Handler<ReqGetTasKReward> {

    static final Logger log = LogManager.getLogger(ReqGetTasKRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetTasKReward messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetTasKRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
