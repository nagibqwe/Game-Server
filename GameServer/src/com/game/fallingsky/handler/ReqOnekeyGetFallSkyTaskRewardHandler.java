package com.game.fallingsky.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FallingSkyMessage.ReqOnekeyGetFallSkyTaskReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一键领取任务奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOnekeyGetFallSkyTaskReward.MsgID.eMsgID_VALUE, clazz = ReqOnekeyGetFallSkyTaskReward.class)

public class ReqOnekeyGetFallSkyTaskRewardHandler extends Handler<ReqOnekeyGetFallSkyTaskReward> {

    static final Logger log = LogManager.getLogger(ReqOnekeyGetFallSkyTaskRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOnekeyGetFallSkyTaskReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.fallingSkyManager.deal().onReqOnekeyGetFallSkyTaskReward(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOnekeyGetFallSkyTaskRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
