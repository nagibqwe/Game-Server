package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服请求跨服BOSS的击杀人
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossSoulAnimalForestBossKiller.MsgID.eMsgID_VALUE, clazz = G2PReqCrossSoulAnimalForestBossKiller.class)

public class G2PReqCrossSoulAnimalForestBossKillerHandler extends Handler<G2PReqCrossSoulAnimalForestBossKiller> {

    static final Logger log = LogManager.getLogger(G2PReqCrossSoulAnimalForestBossKillerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossSoulAnimalForestBossKiller messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onG2PReqCrossSoulAnimalForestBossKiller(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossSoulAnimalForestBossKillerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
