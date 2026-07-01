package com.game.store.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.storeMessage.ReqStoreMoveItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //仓库移动物品消息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqStoreMoveItem.MsgID.eMsgID_VALUE, clazz = ReqStoreMoveItem.class)

public class ReqStoreMoveItemHandler extends Handler<ReqStoreMoveItem> {

    static final Logger log = LogManager.getLogger(ReqStoreMoveItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqStoreMoveItem messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.storeManager.deal().OnReqStoreMoveItem(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqStoreMoveItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
