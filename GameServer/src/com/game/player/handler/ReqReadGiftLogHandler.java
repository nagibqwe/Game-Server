package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqReadGiftLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqReadGiftLog.MsgID.eMsgID_VALUE, clazz = ReqReadGiftLog.class)

public class ReqReadGiftLogHandler extends Handler<ReqReadGiftLog> {

    static final Logger log = LogManager.getLogger(ReqReadGiftLogHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqReadGiftLog messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.managerExt().onReqReadGiftLog(player, messInfo.getIdsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqReadGiftLogHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
