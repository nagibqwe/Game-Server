package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqAutoUseItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //快捷使用背包中的经验丹和银元宝袋
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAutoUseItem.MsgID.eMsgID_VALUE, clazz = ReqAutoUseItem.class)

public class ReqAutoUseItemHandler extends Handler<ReqAutoUseItem> {

    static final Logger log = LogManager.getLogger(ReqAutoUseItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAutoUseItem message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().onReqAutoUseItem(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAutoUseItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
