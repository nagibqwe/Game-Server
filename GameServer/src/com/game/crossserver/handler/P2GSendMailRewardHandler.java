package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GSendMailReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服发送邮件奖励到游戏服
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GSendMailReward.MsgID.eMsgID_VALUE, clazz = P2GSendMailReward.class)

public class P2GSendMailRewardHandler extends Handler<P2GSendMailReward> {

    static final Logger log = LogManager.getLogger(P2GSendMailRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GSendMailReward message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossServerManager.getCrossServer().onP2GSendMailReward( message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
