package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqRefreshShop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 刷新商城
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRefreshShop.MsgID.eMsgID_VALUE, clazz = ReqRefreshShop.class)

public class ReqRefreshShopHandler extends Handler<ReqRefreshShop> {

    static final Logger log = LogManager.getLogger(ReqRefreshShopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRefreshShop messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.shopManager.deal().OnReqRefreshShop(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefreshShopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
