package com.game.cangbaoge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CangbaogeMessage.ReqCangbaogeExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCangbaogeExchange.MsgID.eMsgID_VALUE, clazz = ReqCangbaogeExchange.class)

public class ReqCangbaogeExchangeHandler extends Handler<ReqCangbaogeExchange> {

    static final Logger log = LogManager.getLogger(ReqCangbaogeExchangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCangbaogeExchange message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.cangbaogeManager.deal().ReqCangbaogeExchange(player,message.getExchangeId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCangbaogeExchangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
