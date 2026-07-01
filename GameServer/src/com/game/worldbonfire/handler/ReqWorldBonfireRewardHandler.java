package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.ReqWorldBonfireReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldBonfireReward.MsgID.eMsgID_VALUE, clazz = ReqWorldBonfireReward.class)

public class ReqWorldBonfireRewardHandler extends Handler<ReqWorldBonfireReward> {

    static final Logger log = LogManager.getLogger(ReqWorldBonfireRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldBonfireReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireFingerReward((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldBonfireRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
