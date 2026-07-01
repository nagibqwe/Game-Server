package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqRefuseBuyMarryBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 拒绝购买仙匣
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRefuseBuyMarryBox.MsgID.eMsgID_VALUE, clazz = ReqRefuseBuyMarryBox.class)

public class ReqRefuseBuyMarryBoxHandler extends Handler<ReqRefuseBuyMarryBox> {

    static final Logger log = LogManager.getLogger(ReqRefuseBuyMarryBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRefuseBuyMarryBox messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqRefuseBuyMarryBox(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefuseBuyMarryBoxHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
