package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqCrossFudRank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取福地积分排名
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossFudRank.MsgID.eMsgID_VALUE, clazz = ReqCrossFudRank.class)

public class ReqCrossFudRankHandler extends Handler<ReqCrossFudRank> {

    static final Logger log = LogManager.getLogger(ReqCrossFudRankHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossFudRank message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqCrossFudRankHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
