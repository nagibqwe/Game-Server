package com.game.auction.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AuctionMessage.ReqAuctionInfoOut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //下架
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAuctionInfoOut.MsgID.eMsgID_VALUE, clazz = ReqAuctionInfoOut.class)

public class ReqAuctionInfoOutHandler extends Handler<ReqAuctionInfoOut> {

    static final Logger log = LogManager.getLogger(ReqAuctionInfoOutHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAuctionInfoOut message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.auctionManager.auctionOut(player, message.getAuctionId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAuctionInfoOutHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
