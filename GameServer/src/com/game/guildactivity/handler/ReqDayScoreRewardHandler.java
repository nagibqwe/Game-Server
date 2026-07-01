package com.game.guildactivity.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage.ReqDayScoreReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取日常积分奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDayScoreReward.MsgID.eMsgID_VALUE, clazz = ReqDayScoreReward.class)

public class ReqDayScoreRewardHandler extends Handler<ReqDayScoreReward> {

    static final Logger log = LogManager.getLogger(ReqDayScoreRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDayScoreReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildActivityManager.deal().dayScoreReward(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
