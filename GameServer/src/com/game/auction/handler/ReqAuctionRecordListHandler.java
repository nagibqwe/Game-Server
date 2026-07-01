package com.game.auction.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AuctionMessage.ReqAuctionRecordList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求个人记录
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAuctionRecordList.MsgID.eMsgID_VALUE, clazz = ReqAuctionRecordList.class)

public class ReqAuctionRecordListHandler extends Handler<ReqAuctionRecordList> {

    static final Logger log = LogManager.getLogger(ReqAuctionRecordListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAuctionRecordList message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.auctionManager.manager().auctionRecordList(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAuctionRecordListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
