package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqRechargeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求充值数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRechargeData.MsgID.eMsgID_VALUE, clazz = ReqRechargeData.class)

public class ReqRechargeDataHandler extends Handler<ReqRechargeData> {

    static final Logger log = LogManager.getLogger(ReqRechargeDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRechargeData messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.deal().onReqRechargeData((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRechargeDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
