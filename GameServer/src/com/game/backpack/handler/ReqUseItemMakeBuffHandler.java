package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqUseItemMakeBuff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //检查是否物品使用产生的BUFF
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqUseItemMakeBuff.MsgID.eMsgID_VALUE, clazz = ReqUseItemMakeBuff.class)

public class ReqUseItemMakeBuffHandler extends Handler<ReqUseItemMakeBuff> {

    static final Logger log = LogManager.getLogger(ReqUseItemMakeBuffHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUseItemMakeBuff message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().onReqUseItemMakeBuff(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUseItemMakeBuffHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
