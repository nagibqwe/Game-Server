package com.game.guildactivity.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage.ReqMyFightingBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求我正在攻打的福地boss
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMyFightingBoss.MsgID.eMsgID_VALUE, clazz = ReqMyFightingBoss.class)

public class ReqMyFightingBossHandler extends Handler<ReqMyFightingBoss> {

    static final Logger log = LogManager.getLogger(ReqMyFightingBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMyFightingBoss message) {
        try {
            long start = TimeUtils.Time();

            Manager.guildActivityManager.deal().reqMyFightingBoss( session.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
