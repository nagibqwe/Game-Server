package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqDiscRechargeBuyGoods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //超值折扣购买
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDiscRechargeBuyGoods.MsgID.eMsgID_VALUE, clazz = ReqDiscRechargeBuyGoods.class)

public class ReqDiscRechargeBuyGoodsHandler extends Handler<ReqDiscRechargeBuyGoods> {

    static final Logger log = LogManager.getLogger(ReqDiscRechargeBuyGoodsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDiscRechargeBuyGoods messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();

            Manager.rechargeManager.discountScript().reqDiscRechargeBuyGoods(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDiscRechargeBuyGoodsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
