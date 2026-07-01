package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.P2GPromotion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-玩家晋级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GPromotion.MsgID.eMsgID_VALUE, clazz = P2GPromotion.class)

public class P2GPromotionHandler extends Handler<P2GPromotion> {

    static final Logger log = LogManager.getLogger(P2GPromotionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GPromotion messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().p2GPromotion(messInfo.getType(), messInfo.getIdList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GPromotionHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
