package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.G2PReqEnterHorseBoss;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共服进入战斗
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqEnterHorseBoss.MsgID.eMsgID_VALUE, clazz = G2PReqEnterHorseBoss.class)

public class G2PReqEnterHorseBossHandler extends Handler<G2PReqEnterHorseBoss> {

    static final Logger log = LogManager.getLogger(G2PReqEnterHorseBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqEnterHorseBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossHorseBossManager.deal().onG2PReqEnterHorseBoss(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqEnterHorseBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
