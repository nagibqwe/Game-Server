package com.game.commercialize.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage.ReqDailyRechargeCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求每日累充配置数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDailyRechargeCfg.MsgID.eMsgID_VALUE, clazz = ReqDailyRechargeCfg.class)

public class ReqDailyRechargeCfgHandler extends Handler<ReqDailyRechargeCfg> {

    static final Logger log = LogManager.getLogger(ReqDailyRechargeCfgHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDailyRechargeCfg message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDailyRechargeCfgHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
