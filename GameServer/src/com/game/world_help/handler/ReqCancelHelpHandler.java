package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqCancelHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 取消支援
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCancelHelp.MsgID.eMsgID_VALUE, clazz = ReqCancelHelp.class)

public class ReqCancelHelpHandler extends Handler<ReqCancelHelp> {

    static final Logger log = LogManager.getLogger(ReqCancelHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCancelHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqCancelHelp((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCancelHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
