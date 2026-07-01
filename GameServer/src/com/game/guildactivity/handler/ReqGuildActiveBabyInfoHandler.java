package com.game.guildactivity.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage.ReqGuildActiveBabyInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 公会活跃宝贝活动信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildActiveBabyInfo.MsgID.eMsgID_VALUE, clazz = ReqGuildActiveBabyInfo.class)

public class ReqGuildActiveBabyInfoHandler extends Handler<ReqGuildActiveBabyInfo> {

    static final Logger log = LogManager.getLogger(ReqGuildActiveBabyInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildActiveBabyInfo message) {
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
