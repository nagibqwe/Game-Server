package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqFuncOpenRoleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求功能开关的条件
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFuncOpenRoleInfo.MsgID.eMsgID_VALUE, clazz = ReqFuncOpenRoleInfo.class)

public class ReqFuncOpenRoleInfoHandler extends Handler<ReqFuncOpenRoleInfo> {

    static final Logger log = LogManager.getLogger(ReqFuncOpenRoleInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFuncOpenRoleInfo message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFuncOpenRoleInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
