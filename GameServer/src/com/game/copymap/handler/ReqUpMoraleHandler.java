package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqUpMorale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //鼓舞
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqUpMorale.MsgID.eMsgID_VALUE, clazz = ReqUpMorale.class)

public class ReqUpMoraleHandler extends Handler<ReqUpMorale> {

    static final Logger log = LogManager.getLogger(ReqUpMoraleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUpMorale message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            Manager.copyMapManager.logic().onReqUpMorale(player, message.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpMoraleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
