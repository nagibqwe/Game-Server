package com.game.newfashion.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NewFashionMessage.ReqFashionStar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //升星
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFashionStar.MsgID.eMsgID_VALUE, clazz = ReqFashionStar.class)

public class ReqFashionStarHandler extends Handler<ReqFashionStar> {

    static final Logger log = LogManager.getLogger(ReqFashionStarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFashionStar messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.newFashionManager.deal().ReqFashionStar(player,messInfo.getFashionID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFashionStarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
