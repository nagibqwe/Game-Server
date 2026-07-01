package com.game.fallingsky.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FallingSkyMessage.ReqGetFallSkyLevelReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //等级领奖
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetFallSkyLevelReward.MsgID.eMsgID_VALUE, clazz = ReqGetFallSkyLevelReward.class)

public class ReqGetFallSkyLevelRewardHandler extends Handler<ReqGetFallSkyLevelReward> {

    static final Logger log = LogManager.getLogger(ReqGetFallSkyLevelRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetFallSkyLevelReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.fallingSkyManager.deal().onReqGetFallSkyLevelReward(player,message.getLevelId(),message.getIsFree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
