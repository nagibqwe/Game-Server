package com.game.kaoshangling.handler;

import com.game.kaoshangling.manager.KaoShangLingManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.KaoShangLingMessage.ReqKaoShangLingReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //犒赏令领取奖励请求
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqKaoShangLingReward.MsgID.eMsgID_VALUE, clazz = ReqKaoShangLingReward.class)

public class ReqKaoShangLingRewardHandler extends Handler<ReqKaoShangLingReward> {

    static final Logger log = LogManager.getLogger(ReqKaoShangLingRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqKaoShangLingReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            KaoShangLingManager.getInstance().deal().reqKaoShangLingRewardHandler(player,message.getType(),message.getIsOneKey(),message.getKey(),0);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
