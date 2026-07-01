package com.game.commercialize.handler;

import com.game.commercialize.inter.IDailyRechargeTotal;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CommercializeMessage.ReqGetRechargeReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetRechargeReward.MsgID.eMsgID_VALUE, clazz = ReqGetRechargeReward.class)

public class ReqGetRechargeRewardHandler extends Handler<ReqGetRechargeReward> {

    static final Logger log = LogManager.getLogger(ReqGetRechargeRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetRechargeReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            IDailyRechargeTotal script = (IDailyRechargeTotal) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.NewDailyRecharge);
            if (script != null)
                script.onReqGetRechargeReward(player, message.getRewarId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetRechargeRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
