package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqCallSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 召唤元神
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCallSoul.MsgID.eMsgID_VALUE, clazz = ReqCallSoul.class)

public class ReqCallSoulHandler extends Handler<ReqCallSoul> {

    static final Logger log = LogManager.getLogger(ReqCallSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCallSoul message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqCallSoul(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCallSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
