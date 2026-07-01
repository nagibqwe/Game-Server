package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqOpenMarryChild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 激活仙娃
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenMarryChild.MsgID.eMsgID_VALUE, clazz = ReqOpenMarryChild.class)

public class ReqOpenMarryChildHandler extends Handler<ReqOpenMarryChild> {

    static final Logger log = LogManager.getLogger(ReqOpenMarryChildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenMarryChild messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqOpenMarryChild(mess.getExecutor(), messInfo.getChildId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenMarryChildHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
