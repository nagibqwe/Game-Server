package com.game.treasurehunt.handler;

import com.game.player.structs.Player;
import com.game.treasurehunt.manager.TreasureHuntManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntMessage.ReqBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBuy.MsgID.eMsgID_VALUE, clazz = ReqBuy.class)

public class ReqBuyHandler extends Handler<ReqBuy> {

    static final Logger log = LogManager.getLogger(ReqBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            TreasureHuntManager.getInstance().deal().buy(player, messInfo.getType(), messInfo.getNum(), messInfo.getTimes());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
