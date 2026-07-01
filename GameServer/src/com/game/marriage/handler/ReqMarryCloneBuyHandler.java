package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryCloneBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 购买副本次数
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryCloneBuy.MsgID.eMsgID_VALUE, clazz = ReqMarryCloneBuy.class)

public class ReqMarryCloneBuyHandler extends Handler<ReqMarryCloneBuy> {

    static final Logger log = LogManager.getLogger(ReqMarryCloneBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryCloneBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.clone().reqMarryCloneBuy(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryCloneBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
