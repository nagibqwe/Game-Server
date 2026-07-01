package com.game.recharge.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqCheckGoodsIsCanbuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 客户端请求该商品是否能充值
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCheckGoodsIsCanbuy.MsgID.eMsgID_VALUE, clazz = ReqCheckGoodsIsCanbuy.class)

public class ReqCheckGoodsIsCanbuyHandler extends Handler<ReqCheckGoodsIsCanbuy> {

    static final Logger log = LogManager.getLogger(ReqCheckGoodsIsCanbuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCheckGoodsIsCanbuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.deal().onReqCheckGoodsIsCanbuy( mess.getExecutor(),messInfo.getId(), messInfo.getMoneyType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCheckGoodsIsCanbuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
