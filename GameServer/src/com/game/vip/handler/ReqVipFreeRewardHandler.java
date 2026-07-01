package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqVipFreeReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取免费赠送vip经验奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqVipFreeReward.MsgID.eMsgID_VALUE, clazz = ReqVipFreeReward.class)

public class ReqVipFreeRewardHandler extends Handler<ReqVipFreeReward> {

    static final Logger log = LogManager.getLogger(ReqVipFreeRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqVipFreeReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.deal().getVipFreeReward(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipFreeRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
