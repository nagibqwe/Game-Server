package com.game.newfashion.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NewFashionMessage.ReqSaveFashion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //穿戴时装
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSaveFashion.MsgID.eMsgID_VALUE, clazz = ReqSaveFashion.class)

public class ReqSaveFashionHandler extends Handler<ReqSaveFashion> {

    static final Logger log = LogManager.getLogger(ReqSaveFashionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSaveFashion messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.newFashionManager.deal().ReqSaveFashion(player,messInfo.getWearIdsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSaveFashionHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
