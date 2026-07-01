package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IInvestPeakScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqInvestPeakBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求购买巅峰基金
* @Desc Auto Create
* @Auth Tool
*/

@Message(id = ReqInvestPeakBuy.MsgID.eMsgID_VALUE, clazz = ReqInvestPeakBuy.class)

public class ReqInvestPeakBuyHandler extends Handler<ReqInvestPeakBuy> {

    static final Logger log = LogManager.getLogger(ReqInvestPeakBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqInvestPeakBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IInvestPeakScript script = (IInvestPeakScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.InvestPeak);
            if (script != null)
                script.onReqInvestPeakBuy(player, messInfo.getGear());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqInvestPeakBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
