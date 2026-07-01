package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IExclusiveCardScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqExclusiveCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求购买卡
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqExclusiveCard.MsgID.eMsgID_VALUE, clazz = ReqExclusiveCard.class)

public class ReqExclusiveCardHandler extends Handler<ReqExclusiveCard> {

    static final Logger log = LogManager.getLogger(ReqExclusiveCardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqExclusiveCard messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IExclusiveCardScript script = (IExclusiveCardScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.ExclusiveCard);
            if (script != null)
                script.onReqExclusiveCard(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExclusiveCardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
