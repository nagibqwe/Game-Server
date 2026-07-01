package com.game.dailyactive.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.ReqDailyPushIds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDailyPushIds.MsgID.eMsgID_VALUE, clazz = ReqDailyPushIds.class)

public class ReqDailyPushIdsHandler extends Handler<ReqDailyPushIds> {

    static final Logger log = LogManager.getLogger(ReqDailyPushIdsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDailyPushIds message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.dailyActiveManager.deal().onReqDailyPushIds(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
