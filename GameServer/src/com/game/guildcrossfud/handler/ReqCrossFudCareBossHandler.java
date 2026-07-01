package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqCrossFudCareBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //关注boss
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossFudCareBoss.MsgID.eMsgID_VALUE, clazz = ReqCrossFudCareBoss.class)

public class ReqCrossFudCareBossHandler extends Handler<ReqCrossFudCareBoss> {

    static final Logger log = LogManager.getLogger(ReqCrossFudCareBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossFudCareBoss message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqCrossFudCareBossHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
