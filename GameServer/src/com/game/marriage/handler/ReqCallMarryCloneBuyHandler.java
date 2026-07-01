package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqCallMarryCloneBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 请求对方购买次数
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCallMarryCloneBuy.MsgID.eMsgID_VALUE, clazz = ReqCallMarryCloneBuy.class)

public class ReqCallMarryCloneBuyHandler extends Handler<ReqCallMarryCloneBuy> {

    static final Logger log = LogManager.getLogger(ReqCallMarryCloneBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCallMarryCloneBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.clone().reqCallMarryCloneBuy(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCallMarryCloneBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
