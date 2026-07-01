package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqMoveItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //拆分物品消息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMoveItem.MsgID.eMsgID_VALUE, clazz = ReqMoveItem.class)

public class ReqMoveItemHandler extends Handler<ReqMoveItem> {

    static final Logger log = LogManager.getLogger(ReqMoveItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMoveItem message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().OnReqMoveItem(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMoveItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
