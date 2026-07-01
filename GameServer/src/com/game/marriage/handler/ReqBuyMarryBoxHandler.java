package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqBuyMarryBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 购买仙匣
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBuyMarryBox.MsgID.eMsgID_VALUE, clazz = ReqBuyMarryBox.class)

public class ReqBuyMarryBoxHandler extends Handler<ReqBuyMarryBox> {

    static final Logger log = LogManager.getLogger(ReqBuyMarryBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBuyMarryBox messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqBuyMarryBox(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyMarryBoxHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
