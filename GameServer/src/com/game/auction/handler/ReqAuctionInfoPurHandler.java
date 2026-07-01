package com.game.auction.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AuctionMessage.ReqAuctionInfoPur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一口价购买
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAuctionInfoPur.MsgID.eMsgID_VALUE, clazz = ReqAuctionInfoPur.class)

public class ReqAuctionInfoPurHandler extends Handler<ReqAuctionInfoPur> {

    static final Logger log = LogManager.getLogger(ReqAuctionInfoPurHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAuctionInfoPur message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.auctionManager.auctionPur(player, message.getAuctionId(), message.getPassword());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAuctionInfoPurHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
