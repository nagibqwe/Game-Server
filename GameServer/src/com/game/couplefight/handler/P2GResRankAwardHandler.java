package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GResRankAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-发送排名奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResRankAward.MsgID.eMsgID_VALUE, clazz = P2GResRankAward.class)

public class P2GResRankAwardHandler extends Handler<P2GResRankAward> {

    static final Logger log = LogManager.getLogger(P2GResRankAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResRankAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GResRankAward(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResRankAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
