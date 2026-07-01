package com.game.crosshorseboss.handler;
import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.G2FReqCancelAffiliation;

/**
* makehandler  v1.9 for netty
*取消归属---清除伤害 发送战斗服
*/
@Message(id = G2FReqCancelAffiliation.MsgID.eMsgID_VALUE, clazz = G2FReqCancelAffiliation.class)

public class G2FReqCancelAffiliationHandler extends Handler<G2FReqCancelAffiliation>{

    private static final Logger log = LogManager.getLogger(G2FReqCancelAffiliationHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FReqCancelAffiliation message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossHorseBossManager.deal().onG2FReqCancelAffiliation(message.getPlayerId(),message.getCfgId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqCancelAffiliationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }

    }
}