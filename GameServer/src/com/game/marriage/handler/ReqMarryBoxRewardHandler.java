package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryBoxReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 领取仙匣奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryBoxReward.MsgID.eMsgID_VALUE, clazz = ReqMarryBoxReward.class)

public class ReqMarryBoxRewardHandler extends Handler<ReqMarryBoxReward> {

    static final Logger log = LogManager.getLogger(ReqMarryBoxRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryBoxReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqMarryBoxReward(mess.getExecutor(), messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryBoxRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
