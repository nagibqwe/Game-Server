package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.P2GCrossFudProcess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知福地刷新boss
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCrossFudProcess.MsgID.eMsgID_VALUE, clazz = P2GCrossFudProcess.class)

public class P2GCrossFudProcessHandler extends Handler<P2GCrossFudProcess> {

    static final Logger log = LogManager.getLogger(P2GCrossFudProcessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCrossFudProcess message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().P2GCrossFudProcess(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
