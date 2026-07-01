package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqSellItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //物品出售
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSellItems.MsgID.eMsgID_VALUE, clazz = ReqSellItems.class)

public class ReqSellItemsHandler extends Handler<ReqSellItems> {

    static final Logger log = LogManager.getLogger(ReqSellItemsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSellItems message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().OnReqSellItems(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSellItemsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
