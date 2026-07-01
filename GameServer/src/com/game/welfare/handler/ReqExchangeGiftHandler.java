package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage.ReqExchangeGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求兑换礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqExchangeGift.MsgID.eMsgID_VALUE, clazz = ReqExchangeGift.class)

public class ReqExchangeGiftHandler extends Handler<ReqExchangeGift> {

    static final Logger log = LogManager.getLogger(ReqExchangeGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqExchangeGift messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.activeCodeManager.useActiveCode(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExchangeGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
