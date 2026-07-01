package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.P2GCrossFudBoxUnLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //个人积分解锁
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCrossFudBoxUnLock.MsgID.eMsgID_VALUE, clazz = P2GCrossFudBoxUnLock.class)

public class P2GCrossFudBoxUnLockHandler extends Handler<P2GCrossFudBoxUnLock> {

    static final Logger log = LogManager.getLogger(P2GCrossFudBoxUnLockHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCrossFudBoxUnLock message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().P2GCrossFudBoxUnLock( message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
