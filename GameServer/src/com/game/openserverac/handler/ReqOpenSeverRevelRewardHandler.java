package com.game.openserverac.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenSeverRevelReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领奖请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenSeverRevelReward.MsgID.eMsgID_VALUE, clazz = ReqOpenSeverRevelReward.class)

public class ReqOpenSeverRevelRewardHandler extends Handler<ReqOpenSeverRevelReward> {

    static final Logger log = LogManager.getLogger(ReqOpenSeverRevelRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenSeverRevelReward messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenSeverRevelRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
