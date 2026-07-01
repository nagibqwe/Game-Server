package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqRefuseMarryCloneBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 拒绝购买
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRefuseMarryCloneBuy.MsgID.eMsgID_VALUE, clazz = ReqRefuseMarryCloneBuy.class)

public class ReqRefuseMarryCloneBuyHandler extends Handler<ReqRefuseMarryCloneBuy> {

    static final Logger log = LogManager.getLogger(ReqRefuseMarryCloneBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRefuseMarryCloneBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.clone().reqRefuseMarryClone(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefuseMarryCloneBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
