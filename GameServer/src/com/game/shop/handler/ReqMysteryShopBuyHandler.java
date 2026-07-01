package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqMysteryShopBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求购买神秘商店商品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMysteryShopBuy.MsgID.eMsgID_VALUE, clazz = ReqMysteryShopBuy.class)

public class ReqMysteryShopBuyHandler extends Handler<ReqMysteryShopBuy> {

    static final Logger log = LogManager.getLogger(ReqMysteryShopBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMysteryShopBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.shopManager.mysteryShop().onReqMysteryShopBuy(player,messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMysteryShopBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
