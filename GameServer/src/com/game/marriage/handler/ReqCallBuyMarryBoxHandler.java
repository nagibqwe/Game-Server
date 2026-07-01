package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqCallBuyMarryBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 请求伴侣购买仙匣
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCallBuyMarryBox.MsgID.eMsgID_VALUE, clazz = ReqCallBuyMarryBox.class)

public class ReqCallBuyMarryBoxHandler extends Handler<ReqCallBuyMarryBox> {

    static final Logger log = LogManager.getLogger(ReqCallBuyMarryBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCallBuyMarryBox messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqCallBuyMarryBox(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCallBuyMarryBoxHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
