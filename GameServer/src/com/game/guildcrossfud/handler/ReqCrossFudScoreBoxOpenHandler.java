package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqCrossFudScoreBoxOpen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取积分宝箱奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossFudScoreBoxOpen.MsgID.eMsgID_VALUE, clazz = ReqCrossFudScoreBoxOpen.class)

public class ReqCrossFudScoreBoxOpenHandler extends Handler<ReqCrossFudScoreBoxOpen> {

    static final Logger log = LogManager.getLogger(ReqCrossFudScoreBoxOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossFudScoreBoxOpen message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqCrossFudScoreBoxOpenHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
