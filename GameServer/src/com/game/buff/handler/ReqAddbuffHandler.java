package com.game.buff.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BuffMessage.ReqAddbuff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //载具 变身处理
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAddbuff.MsgID.eMsgID_VALUE, clazz = ReqAddbuff.class)

public class ReqAddbuffHandler extends Handler<ReqAddbuff> {

    static final Logger log = LogManager.getLogger(ReqAddbuffHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAddbuff message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.buffManager.deal().onReqAddChangeModeBuff(player,message.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddbuffHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
