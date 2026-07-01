package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqChildCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 仙娃出战||召回
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChildCall.MsgID.eMsgID_VALUE, clazz = ReqChildCall.class)

public class ReqChildCallHandler extends Handler<ReqChildCall> {

    static final Logger log = LogManager.getLogger(ReqChildCallHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChildCall messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqChildCall(mess.getExecutor(), messInfo.getChildId(), messInfo.getOpt());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChildCallHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
