package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IGrowthFundScript;
import com.game.welfare.script.IInvestPeakScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqInvestPeakServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取巅峰基金全服奖励
* @Desc Auto Create
* @Auth Tool
*/

@Message(id = ReqInvestPeakServer.MsgID.eMsgID_VALUE, clazz = ReqInvestPeakServer.class)

public class ReqInvestPeakServerHandler extends Handler<ReqInvestPeakServer> {

    static final Logger log = LogManager.getLogger(ReqInvestPeakServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqInvestPeakServer messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IInvestPeakScript script = (IInvestPeakScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.InvestPeak);
            if (script != null)
                script.onReqInvestPeakServer(player, messInfo.getCfgID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqInvestPeakServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
