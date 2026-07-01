package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqOpenCoupleShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenCoupleShop.MsgID.eMsgID_VALUE, clazz = ReqOpenCoupleShop.class)

public class ReqOpenCoupleShopHandler extends Handler<ReqOpenCoupleShop> {

    static final Logger log = LogManager.getLogger(ReqOpenCoupleShopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenCoupleShop messInfo) {
        try {
            long start = TimeUtils.Time();


            Manager.couplefightManager.getCoupleShop().onReqOpenCoupleShop((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenCoupleShopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
