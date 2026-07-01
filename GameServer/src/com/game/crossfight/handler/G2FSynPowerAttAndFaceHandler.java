package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FSynPowerAttAndFace;
import io.netty.channel.ChannelHandlerContext;

/**
 * makehandler v1.6 for netty game向战斗服同步战斗力数据及外观信息
 */
@Message(id = G2FSynPowerAttAndFace.MsgID.eMsgID_VALUE, clazz = G2FSynPowerAttAndFace.class)
public class G2FSynPowerAttAndFaceHandler extends Handler<G2FSynPowerAttAndFace> {

    private static final Logger log = LogManager.getLogger(G2FSynPowerAttAndFaceHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FSynPowerAttAndFace message) {

        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnG2FSynPowerAttAndFace(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FSynPowerAttAndFaceHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
