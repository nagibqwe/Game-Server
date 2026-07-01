package com.game.map.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqCineMatic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //剧情播放玩家位置变更请求
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCineMatic.MsgID.eMsgID_VALUE, clazz = ReqCineMatic.class)

public class ReqCineMaticHandler extends Handler<ReqCineMatic> {

    static final Logger log = LogManager.getLogger(ReqCineMaticHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCineMatic message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
