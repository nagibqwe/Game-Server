package com.game.crossrank.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage.ReqG2PSyncCrossRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步玩家排行数据到公共服
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqG2PSyncCrossRankInfo.MsgID.eMsgID_VALUE, clazz = ReqG2PSyncCrossRankInfo.class)

public class ReqG2PSyncCrossRankInfoHandler extends Handler<ReqG2PSyncCrossRankInfo> {

    static final Logger log = LogManager.getLogger(ReqG2PSyncCrossRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqG2PSyncCrossRankInfo message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqG2PSyncCrossRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
