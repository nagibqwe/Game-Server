package com.game.cangbaoge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CangbaogeMessage.ReqOpenCangbaogeExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开藏宝阁兑换界面
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenCangbaogeExchange.MsgID.eMsgID_VALUE, clazz = ReqOpenCangbaogeExchange.class)

public class ReqOpenCangbaogeExchangeHandler extends Handler<ReqOpenCangbaogeExchange> {

    static final Logger log = LogManager.getLogger(ReqOpenCangbaogeExchangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenCangbaogeExchange message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.cangbaogeManager.deal().ReqOpenCangbaogeExchange(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenCangbaogeExchangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
