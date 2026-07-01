package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqThkHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 感谢
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqThkHelp.MsgID.eMsgID_VALUE, clazz = ReqThkHelp.class)

public class ReqThkHelpHandler extends Handler<ReqThkHelp> {

    static final Logger log = LogManager.getLogger(ReqThkHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqThkHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqThkHelp((Player) mess.getExecutor(), messInfo.getId(), messInfo.getWords());


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqThkHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
