package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqBuyCoupleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //购买商品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBuyCoupleItem.MsgID.eMsgID_VALUE, clazz = ReqBuyCoupleItem.class)

public class ReqBuyCoupleItemHandler extends Handler<ReqBuyCoupleItem> {

    static final Logger log = LogManager.getLogger(ReqBuyCoupleItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBuyCoupleItem messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.couplefightManager.getCoupleShop().onReqBuyCoupleItem((Player) mess.getExecutor(),messInfo.getId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyCoupleItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
