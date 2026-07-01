package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求是否关注跨服BOSS的关注
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqFollowSoulAnimalForestCrossBoss.MsgID.eMsgID_VALUE, clazz = G2PReqFollowSoulAnimalForestCrossBoss.class)

public class G2PReqFollowSoulAnimalForestCrossBossHandler extends Handler<G2PReqFollowSoulAnimalForestCrossBoss> {

    static final Logger log = LogManager.getLogger(G2PReqFollowSoulAnimalForestCrossBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqFollowSoulAnimalForestCrossBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onG2PReqFollowSoulAnimalForestCrossBoss(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqFollowSoulAnimalForestCrossBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
