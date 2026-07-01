package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqCloseCloudBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家关闭云购活动面板
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCloseCloudBuy.MsgID.eMsgID_VALUE, clazz = ReqCloseCloudBuy.class)

public class ReqCloseCloudBuyHandler extends Handler<ReqCloseCloudBuy> {

    static final Logger log = LogManager.getLogger(ReqCloseCloudBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCloseCloudBuy message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCloseCloudBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
