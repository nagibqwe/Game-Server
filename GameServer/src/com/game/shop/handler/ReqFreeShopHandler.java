package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqFreeShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求0元购
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFreeShop.MsgID.eMsgID_VALUE, clazz = ReqFreeShop.class)

public class ReqFreeShopHandler extends Handler<ReqFreeShop> {

    static final Logger log = LogManager.getLogger(ReqFreeShopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFreeShop messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.shopManager.freeShop().onReqFreeShop((Player) mess.getExecutor(), messInfo.getId(),messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFreeShopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
