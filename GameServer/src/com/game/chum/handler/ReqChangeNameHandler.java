package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqChangeName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 改名
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeName.MsgID.eMsgID_VALUE, clazz = ReqChangeName.class)

public class ReqChangeNameHandler extends Handler<ReqChangeName> {

    static final Logger log = LogManager.getLogger(ReqChangeNameHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeName message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqChangeName(player, message.getName());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeNameHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
