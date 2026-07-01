package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GResCrossDropCoin;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //战斗服通知玩家的经验及金币增长
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GResCrossDropCoin.MsgID.eMsgID_VALUE, clazz = F2GResCrossDropCoin.class)

public class F2GResCrossDropCoinHandler extends Handler<F2GResCrossDropCoin> {

    static final Logger log = LogManager.getLogger(F2GResCrossDropCoinHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GResCrossDropCoin message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GResCrossDropCoin(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GResCrossDropCoinHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
