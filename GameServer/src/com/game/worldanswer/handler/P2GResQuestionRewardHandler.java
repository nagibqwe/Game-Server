package com.game.worldanswer.handler;

import com.game.worldanswer.manager.WorldAnswerManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.P2GResQuestionReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服告诉游戏服发答题奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResQuestionReward.MsgID.eMsgID_VALUE, clazz = P2GResQuestionReward.class)

public class P2GResQuestionRewardHandler extends Handler<P2GResQuestionReward> {

    static final Logger log = LogManager.getLogger(P2GResQuestionRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResQuestionReward messInfo) {
        try {
            long start = TimeUtils.Time();

            WorldAnswerManager.getInstance().getIWorldAnswer().sendAnswerReward(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResQuestionRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
