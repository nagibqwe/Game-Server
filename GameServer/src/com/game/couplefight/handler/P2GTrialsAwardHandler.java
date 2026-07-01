package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GTrialsAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-海选赛奖励发送
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GTrialsAward.MsgID.eMsgID_VALUE, clazz = P2GTrialsAward.class)

public class P2GTrialsAwardHandler extends Handler<P2GTrialsAward> {

    static final Logger log = LogManager.getLogger(P2GTrialsAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GTrialsAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GTrialsAward(messInfo.getAwardList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GTrialsAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
