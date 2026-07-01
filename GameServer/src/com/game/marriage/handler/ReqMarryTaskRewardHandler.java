package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryTaskReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> 领取仙缘任务奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryTaskReward.MsgID.eMsgID_VALUE, clazz = ReqMarryTaskReward.class)

public class ReqMarryTaskRewardHandler extends Handler<ReqMarryTaskReward> {

    static final Logger log = LogManager.getLogger(ReqMarryTaskRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryTaskReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqMarryTaskReward(mess.getExecutor(), messInfo.getTaskId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryTaskRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
