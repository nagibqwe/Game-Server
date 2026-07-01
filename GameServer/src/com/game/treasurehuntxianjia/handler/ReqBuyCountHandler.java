package com.game.treasurehuntxianjia.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntXianjiaMessage.ReqBuyCount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBuyCount.MsgID.eMsgID_VALUE, clazz = ReqBuyCount.class)

public class ReqBuyCountHandler extends Handler<ReqBuyCount> {

    static final Logger log = LogManager.getLogger(ReqBuyCountHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBuyCount messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntXianjiaManager.deal().onReqBuyCount(player,messInfo.getType(),messInfo.getNum(),messInfo.getTimes());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyCountHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
