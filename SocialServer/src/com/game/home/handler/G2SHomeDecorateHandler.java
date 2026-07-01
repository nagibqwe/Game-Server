package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SHomeDecorate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //布置家具
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SHomeDecorate.MsgID.eMsgID_VALUE, clazz = G2SHomeDecorate.class)

public class G2SHomeDecorateHandler extends Handler<G2SHomeDecorate> {

    static final Logger log = LogManager.getLogger(G2SHomeDecorateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SHomeDecorate messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SHomeDecorate(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SHomeDecorateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
