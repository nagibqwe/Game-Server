package com.game.world_help.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqDieCallHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 死亡请求支援
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDieCallHelp.MsgID.eMsgID_VALUE, clazz = ReqDieCallHelp.class)

public class ReqDieCallHelpHandler extends Handler<ReqDieCallHelp> {

    static final Logger log = LogManager.getLogger(ReqDieCallHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDieCallHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().reqDieCallHelp( mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDieCallHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
