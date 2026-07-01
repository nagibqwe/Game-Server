package com.game.crossrank.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage.ReqG2PCrossRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服排行信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqG2PCrossRankInfo.MsgID.eMsgID_VALUE, clazz = ReqG2PCrossRankInfo.class)

public class ReqG2PCrossRankInfoHandler extends Handler<ReqG2PCrossRankInfo> {

    static final Logger log = LogManager.getLogger(ReqG2PCrossRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqG2PCrossRankInfo message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqG2PCrossRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
