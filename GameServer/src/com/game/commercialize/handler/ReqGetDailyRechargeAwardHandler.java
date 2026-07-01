package com.game.commercialize.handler;

import com.game.commercialize.inter.IDailyRecharge;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CommercializeMessage.ReqGetDailyRechargeAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取每日累充奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetDailyRechargeAward.MsgID.eMsgID_VALUE, clazz = ReqGetDailyRechargeAward.class)

public class ReqGetDailyRechargeAwardHandler extends Handler<ReqGetDailyRechargeAward> {

    static final Logger log = LogManager.getLogger(ReqGetDailyRechargeAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetDailyRechargeAward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            IDailyRecharge script = (IDailyRecharge) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.DailyRecharge);
            if (script != null)
                script.onReqDailyAccRechargeAward(player, message.getAwardId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetDailyRechargeAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
