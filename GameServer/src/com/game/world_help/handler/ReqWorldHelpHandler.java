package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqWorldHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求世界支援
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldHelp.MsgID.eMsgID_VALUE, clazz = ReqWorldHelp.class)

public class ReqWorldHelpHandler extends Handler<ReqWorldHelp> {

    static final Logger log = LogManager.getLogger(ReqWorldHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqWorldHelp((Player) mess.getExecutor(), messInfo.getBossCode());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
