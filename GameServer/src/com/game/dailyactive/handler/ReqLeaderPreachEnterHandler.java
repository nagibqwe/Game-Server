package com.game.dailyactive.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.ReqLeaderPreachEnter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc // 请求进入掌门传道
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqLeaderPreachEnter.MsgID.eMsgID_VALUE, clazz = ReqLeaderPreachEnter.class)

public class ReqLeaderPreachEnterHandler extends Handler<ReqLeaderPreachEnter> {

    static final Logger log = LogManager.getLogger(ReqLeaderPreachEnterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqLeaderPreachEnter message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            Manager.leaderPreachManager.getScript().onReqLeaderPreachEnter(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
