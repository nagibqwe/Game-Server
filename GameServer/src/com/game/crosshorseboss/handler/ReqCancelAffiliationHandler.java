package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.ReqCancelAffiliation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //取消归属---清除伤害
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCancelAffiliation.MsgID.eMsgID_VALUE, clazz = ReqCancelAffiliation.class)

public class ReqCancelAffiliationHandler extends Handler<ReqCancelAffiliation> {

    static final Logger log = LogManager.getLogger(ReqCancelAffiliationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCancelAffiliation message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.crossHorseBossManager.deal().onReqCancelAffiliation(player,message.getCfgId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCancelAffiliationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
