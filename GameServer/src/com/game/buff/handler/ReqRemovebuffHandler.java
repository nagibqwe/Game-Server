package com.game.buff.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BuffMessage.ReqRemovebuff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRemovebuff.MsgID.eMsgID_VALUE, clazz = ReqRemovebuff.class)

public class ReqRemovebuffHandler extends Handler<ReqRemovebuff> {

    static final Logger log = LogManager.getLogger(ReqRemovebuffHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRemovebuff message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.buffManager.deal().onReqRemoveChangeModeBuff(player,message.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRemovebuffHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
