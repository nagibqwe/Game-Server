package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqAllCrossFudInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服福地数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAllCrossFudInfo.MsgID.eMsgID_VALUE, clazz = ReqAllCrossFudInfo.class)

public class ReqAllCrossFudInfoHandler extends Handler<ReqAllCrossFudInfo> {

    static final Logger log = LogManager.getLogger(ReqAllCrossFudInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAllCrossFudInfo message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqAllCrossFudInfoHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
