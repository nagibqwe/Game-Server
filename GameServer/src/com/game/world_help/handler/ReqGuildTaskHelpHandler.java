package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqGuildTaskHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求发起公会任务支援
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGuildTaskHelp.MsgID.eMsgID_VALUE, clazz = ReqGuildTaskHelp.class)

public class ReqGuildTaskHelpHandler extends Handler<ReqGuildTaskHelp> {

    static final Logger log = LogManager.getLogger(ReqGuildTaskHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGuildTaskHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqGuildTaskHelp((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGuildTaskHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
