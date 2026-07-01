package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqBuyItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 购买物品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBuyItem.MsgID.eMsgID_VALUE, clazz = ReqBuyItem.class)

public class ReqBuyItemHandler extends Handler<ReqBuyItem> {

    static final Logger log = LogManager.getLogger(ReqBuyItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBuyItem messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.shopManager.deal().OnReqBuyItem(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
