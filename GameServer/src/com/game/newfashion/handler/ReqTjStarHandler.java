package com.game.newfashion.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NewFashionMessage.ReqTjStar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //图鉴升星
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTjStar.MsgID.eMsgID_VALUE, clazz = ReqTjStar.class)

public class ReqTjStarHandler extends Handler<ReqTjStar> {

    static final Logger log = LogManager.getLogger(ReqTjStarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTjStar messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.newFashionManager.deal().ReqTjStar(player,messInfo.getTjID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTjStarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
