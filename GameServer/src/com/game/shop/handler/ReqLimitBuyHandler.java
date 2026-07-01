package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqLimitBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求购买神秘限购的商品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLimitBuy.MsgID.eMsgID_VALUE, clazz = ReqLimitBuy.class)

public class ReqLimitBuyHandler extends Handler<ReqLimitBuy> {

    static final Logger log = LogManager.getLogger(ReqLimitBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLimitBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.shopManager.limitShop().onReqLimitBuy((Player) mess.getExecutor(), messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLimitBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
