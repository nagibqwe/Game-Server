package com.game.openserverac.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqV4RebateReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqV4RebateReward.MsgID.eMsgID_VALUE, clazz = ReqV4RebateReward.class)

public class ReqV4RebateRewardHandler extends Handler<ReqV4RebateReward> {

    static final Logger log = LogManager.getLogger(ReqV4RebateRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqV4RebateReward messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.openServerAcManager.v4Rebate().onReqV4RebeteReward(mess.getExecutor(),messInfo.getRewardState());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqV4RebateRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
