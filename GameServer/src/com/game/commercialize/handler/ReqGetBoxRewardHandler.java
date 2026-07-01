package com.game.commercialize.handler;

import com.game.commercialize.inter.IDailyRecharge;
import com.game.commercialize.inter.IDailyRechargeTotal;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CommercializeMessage.ReqGetBoxReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //宝箱奖励领取
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetBoxReward.MsgID.eMsgID_VALUE, clazz = ReqGetBoxReward.class)

public class ReqGetBoxRewardHandler extends Handler<ReqGetBoxReward> {

    static final Logger log = LogManager.getLogger(ReqGetBoxRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetBoxReward messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();
            IDailyRechargeTotal script = (IDailyRechargeTotal) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.NewDailyRecharge);
            if (script != null)
                script.onReqGetBoxReward(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetBoxRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
