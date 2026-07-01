package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryChildChangeName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 仙娃改名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryChildChangeName.MsgID.eMsgID_VALUE, clazz = ReqMarryChildChangeName.class)

public class ReqMarryChildChangeNameHandler extends Handler<ReqMarryChildChangeName> {

    static final Logger log = LogManager.getLogger(ReqMarryChildChangeNameHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryChildChangeName messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqMarryChildChangeName(mess.getExecutor(), messInfo.getChildId(), messInfo.getName());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryChildChangeNameHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
