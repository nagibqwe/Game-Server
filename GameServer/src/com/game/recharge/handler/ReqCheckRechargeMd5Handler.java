package com.game.recharge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RechargeMessage.ReqCheckRechargeMd5;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCheckRechargeMd5.MsgID.eMsgID_VALUE, clazz = ReqCheckRechargeMd5.class)

public class ReqCheckRechargeMd5Handler extends Handler<ReqCheckRechargeMd5> {

    static final Logger log = LogManager.getLogger(ReqCheckRechargeMd5Handler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCheckRechargeMd5 messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.rechargeManager.deal().onReqCheckRechargeMd5((Player) mess.getExecutor(),messInfo.getMd5());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCheckRechargeMd5Handler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
