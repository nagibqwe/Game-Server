package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqGetFreeDiscGoods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取免费超值折扣奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetFreeDiscGoods.MsgID.eMsgID_VALUE, clazz = ReqGetFreeDiscGoods.class)

public class ReqGetFreeDiscGoodsHandler extends Handler<ReqGetFreeDiscGoods> {

    static final Logger log = LogManager.getLogger(ReqGetFreeDiscGoodsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetFreeDiscGoods messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            Manager.rechargeManager.discountScript().reqGetFreeDiscGoods(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetFreeDiscGoodsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
