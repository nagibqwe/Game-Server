package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqRecharge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 内部充值
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRecharge.MsgID.eMsgID_VALUE, clazz = ReqRecharge.class)

public class ReqRechargeHandler extends Handler<ReqRecharge> {

    static final Logger log = LogManager.getLogger(ReqRechargeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRecharge messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.deal().onReqRecharge((Player) mess.getExecutor(), messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRechargeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
