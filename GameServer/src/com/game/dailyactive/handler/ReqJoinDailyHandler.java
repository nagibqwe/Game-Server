package com.game.dailyactive.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.ReqJoinDaily;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //参加daily活动
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqJoinDaily.MsgID.eMsgID_VALUE, clazz = ReqJoinDaily.class)

public class ReqJoinDailyHandler extends Handler<ReqJoinDaily> {

    static final Logger log = LogManager.getLogger(ReqJoinDailyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqJoinDaily message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.dailyActiveManager.deal().joinDailyActive(player, message.getDailyId(), message.getParam(), false);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
