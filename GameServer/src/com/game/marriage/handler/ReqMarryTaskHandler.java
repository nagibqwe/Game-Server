package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> 请求仙缘任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryTask.MsgID.eMsgID_VALUE, clazz = ReqMarryTask.class)

public class ReqMarryTaskHandler extends Handler<ReqMarryTask> {

    static final Logger log = LogManager.getLogger(ReqMarryTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryTask messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().sendMarryTask( mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
