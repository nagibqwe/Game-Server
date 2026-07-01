package com.game.commercialize.handler;

import com.game.commercialize.inter.IFCCharge;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CommercializeMessage.ReqFCChargeReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取首充续充奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFCChargeReward.MsgID.eMsgID_VALUE, clazz = ReqFCChargeReward.class)

public class ReqFCChargeRewardHandler extends Handler<ReqFCChargeReward> {

    static final Logger log = LogManager.getLogger(ReqFCChargeRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFCChargeReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            IFCCharge script = (IFCCharge) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.FCCharge);
            if (script != null)
                script.onReqFCChargeReward(player, message.getCfgID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFCChargeRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
