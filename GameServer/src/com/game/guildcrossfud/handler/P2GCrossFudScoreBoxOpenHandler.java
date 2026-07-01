package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.P2GCrossFudScoreBoxOpen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //个人积分宝箱打开
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCrossFudScoreBoxOpen.MsgID.eMsgID_VALUE, clazz = P2GCrossFudScoreBoxOpen.class)

public class P2GCrossFudScoreBoxOpenHandler extends Handler<P2GCrossFudScoreBoxOpen> {

    static final Logger log = LogManager.getLogger(P2GCrossFudScoreBoxOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCrossFudScoreBoxOpen message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().P2GCrossFudScoreBoxOpen(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
