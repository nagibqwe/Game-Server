package com.game.alienboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.G2PEnterCrossAlienGem;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入须弥宝库
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PEnterCrossAlienGem.MsgID.eMsgID_VALUE, clazz = G2PEnterCrossAlienGem.class)

public class G2PEnterCrossAlienGemHandler extends Handler<G2PEnterCrossAlienGem> {

    static final Logger log = LogManager.getLogger(G2PEnterCrossAlienGemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PEnterCrossAlienGem messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.alien().G2PEnterCrossAlienGem(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PEnterCrossAlienGemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
