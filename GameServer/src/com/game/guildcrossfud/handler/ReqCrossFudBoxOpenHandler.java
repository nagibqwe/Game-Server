package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqCrossFudBoxOpen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取福地宝箱奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossFudBoxOpen.MsgID.eMsgID_VALUE, clazz = ReqCrossFudBoxOpen.class)

public class ReqCrossFudBoxOpenHandler extends Handler<ReqCrossFudBoxOpen> {

    static final Logger log = LogManager.getLogger(ReqCrossFudBoxOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossFudBoxOpen message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqCrossFudBoxOpenHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
