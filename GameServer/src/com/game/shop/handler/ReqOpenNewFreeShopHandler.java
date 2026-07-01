package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqOpenNewFreeShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开购买面板
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenNewFreeShop.MsgID.eMsgID_VALUE, clazz = ReqOpenNewFreeShop.class)

public class ReqOpenNewFreeShopHandler extends Handler<ReqOpenNewFreeShop> {

    static final Logger log = LogManager.getLogger(ReqOpenNewFreeShopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenNewFreeShop messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.shopManager.newFreeShop().onReqOpenNewFreeShop((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenNewFreeShopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
