package com.game.recharge.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqCheckDiscRechargeGoods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //检测超值折扣商品是否有可购买
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCheckDiscRechargeGoods.MsgID.eMsgID_VALUE, clazz = ReqCheckDiscRechargeGoods.class)

public class ReqCheckDiscRechargeGoodsHandler extends Handler<ReqCheckDiscRechargeGoods> {

    static final Logger log = LogManager.getLogger(ReqCheckDiscRechargeGoodsHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCheckDiscRechargeGoods messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.discountScript().checkDiscountRecharge(mess.getExecutor(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCheckDiscRechargeGoodsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
