package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqVipRechargeReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取累充奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqVipRechargeReward.MsgID.eMsgID_VALUE, clazz = ReqVipRechargeReward.class)

public class ReqVipRechargeRewardHandler extends Handler<ReqVipRechargeReward> {

    static final Logger log = LogManager.getLogger(ReqVipRechargeRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqVipRechargeReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.deal().getVipRechageReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipRechargeRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
