package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqChangeAnno;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 改公告
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeAnno.MsgID.eMsgID_VALUE, clazz = ReqChangeAnno.class)

public class ReqChangeAnnoHandler extends Handler<ReqChangeAnno> {

    static final Logger log = LogManager.getLogger(ReqChangeAnnoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeAnno message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqChangeAnno(player, message.getAnno());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeAnnoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
