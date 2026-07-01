package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IGrowthFundScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqGrowthGetAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取成长基金奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGrowthGetAward.MsgID.eMsgID_VALUE, clazz = ReqGrowthGetAward.class)

public class ReqGrowthGetAwardHandler extends Handler<ReqGrowthGetAward> {

    static final Logger log = LogManager.getLogger(ReqGrowthGetAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGrowthGetAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IGrowthFundScript script = (IGrowthFundScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.GrowthFund);
            if (script != null)
                script.onReqGrowthGetAward(player, messInfo.getCfgID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGrowthGetAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
