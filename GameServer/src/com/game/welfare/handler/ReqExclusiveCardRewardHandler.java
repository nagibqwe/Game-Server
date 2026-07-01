package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IExclusiveCardScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqExclusiveCardReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqExclusiveCardReward.MsgID.eMsgID_VALUE, clazz = ReqExclusiveCardReward.class)

public class ReqExclusiveCardRewardHandler extends Handler<ReqExclusiveCardReward> {

    static final Logger log = LogManager.getLogger(ReqExclusiveCardRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqExclusiveCardReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IExclusiveCardScript script = (IExclusiveCardScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExclusiveCard);
            if (script != null)
                script.onReqExclusiveCardReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExclusiveCardRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
