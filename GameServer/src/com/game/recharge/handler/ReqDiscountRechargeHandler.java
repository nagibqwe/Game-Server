package com.game.recharge.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqDiscountRecharge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求超值折扣数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDiscountRecharge.MsgID.eMsgID_VALUE, clazz = ReqDiscountRecharge.class)

public class ReqDiscountRechargeHandler extends Handler<ReqDiscountRecharge> {

    static final Logger log = LogManager.getLogger(ReqDiscountRechargeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDiscountRecharge messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.discountScript().reqDiscountRecharge(mess.getExecutor(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDiscountRechargeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
