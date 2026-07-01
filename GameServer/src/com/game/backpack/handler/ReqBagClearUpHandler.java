package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqBagClearUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //背包整理
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBagClearUp.MsgID.eMsgID_VALUE, clazz = ReqBagClearUp.class)

public class ReqBagClearUpHandler extends Handler<ReqBagClearUp> {

    static final Logger log = LogManager.getLogger(ReqBagClearUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBagClearUp message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().OnReqBagClearUp(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBagClearUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
