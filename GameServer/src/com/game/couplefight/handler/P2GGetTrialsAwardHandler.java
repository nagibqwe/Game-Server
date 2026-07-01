package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GGetTrialsAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-返回玩家领取奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GGetTrialsAward.MsgID.eMsgID_VALUE, clazz = P2GGetTrialsAward.class)

public class P2GGetTrialsAwardHandler extends Handler<P2GGetTrialsAward> {

    static final Logger log = LogManager.getLogger(P2GGetTrialsAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GGetTrialsAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GGetTrialsAward(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GGetTrialsAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
