package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.G2PReqFollowCrossHorseBoss;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求是否关注跨服BOSS的关注
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqFollowCrossHorseBoss.MsgID.eMsgID_VALUE, clazz = G2PReqFollowCrossHorseBoss.class)

public class G2PReqFollowCrossHorseBossHandler extends Handler<G2PReqFollowCrossHorseBoss> {

    static final Logger log = LogManager.getLogger(G2PReqFollowCrossHorseBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqFollowCrossHorseBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossHorseBossManager.deal().onG2PReqFollowCrossHorseBoss(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqFollowCrossHorseBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
