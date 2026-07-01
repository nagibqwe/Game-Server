package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqVipReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取每日礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqVipReward.MsgID.eMsgID_VALUE, clazz = ReqVipReward.class)

public class ReqVipRewardHandler extends Handler<ReqVipReward> {

    static final Logger log = LogManager.getLogger(ReqVipRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqVipReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.deal().getVipReward(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
