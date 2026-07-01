package com.game.newfashion.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NewFashionMessage.ReqActiveFashion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //---时装激活
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveFashion.MsgID.eMsgID_VALUE, clazz = ReqActiveFashion.class)

public class ReqActiveFashionHandler extends Handler<ReqActiveFashion> {

    static final Logger log = LogManager.getLogger(ReqActiveFashionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveFashion messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.newFashionManager.deal().ReqActiveFashion(player,messInfo.getFashionID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveFashionHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
