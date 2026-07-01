package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGrowUpPointReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取积分奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGrowUpPointReward.MsgID.eMsgID_VALUE, clazz = ReqGrowUpPointReward.class)

public class ReqGrowUpPointRewardHandler extends Handler<ReqGrowUpPointReward> {

    static final Logger log = LogManager.getLogger(ReqGrowUpPointRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGrowUpPointReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqGrowUpReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGrowUpPointRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
