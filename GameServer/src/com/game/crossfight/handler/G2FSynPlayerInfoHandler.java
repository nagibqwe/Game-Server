package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FSynPlayerInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * makehandler v1.6 for netty game 向fight同步玩家信息
 */
@Message(id = G2FSynPlayerInfo.MsgID.eMsgID_VALUE, clazz = G2FSynPlayerInfo.class)
public class G2FSynPlayerInfoHandler extends Handler<G2FSynPlayerInfo> {

    private static final Logger log = LogManager.getLogger(G2FSynPlayerInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FSynPlayerInfo message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnG2FSynPlayerInfo(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FSynPlayerInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
