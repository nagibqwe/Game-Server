package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenServerSpecExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求兑换
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenServerSpecExchange.MsgID.eMsgID_VALUE, clazz = ReqOpenServerSpecExchange.class)

public class ReqOpenServerSpecExchangeHandler extends Handler<ReqOpenServerSpecExchange> {

    static final Logger log = LogManager.getLogger(ReqOpenServerSpecExchangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenServerSpecExchange messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqOpenServerSpeceExchange(player, messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenServerSpecExchangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
