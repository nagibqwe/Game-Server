package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.P2GCrossFudOwnerNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知福地占领奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCrossFudOwnerNotice.MsgID.eMsgID_VALUE, clazz = P2GCrossFudOwnerNotice.class)

public class P2GCrossFudOwnerNoticeHandler extends Handler<P2GCrossFudOwnerNotice> {

    static final Logger log = LogManager.getLogger(P2GCrossFudOwnerNoticeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCrossFudOwnerNotice message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().P2GCrossFudOwnerNotice(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
