package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IGrowthFundScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqGrowthFundServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取成长基金全服奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGrowthFundServer.MsgID.eMsgID_VALUE, clazz = ReqGrowthFundServer.class)

public class ReqGrowthFundServerHandler extends Handler<ReqGrowthFundServer> {

    static final Logger log = LogManager.getLogger(ReqGrowthFundServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGrowthFundServer messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IGrowthFundScript script = (IGrowthFundScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.GrowthFund);
            if (script != null)
                script.onReqGrowthFundServer(player, messInfo.getCfgID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGrowthFundServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
