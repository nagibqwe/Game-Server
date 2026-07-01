package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.G2PEnterPeakMatch;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PEnterPeakMatch.MsgID.eMsgID_VALUE, clazz = G2PEnterPeakMatch.class)

public class G2PEnterPeakMatchHandler extends Handler<G2PEnterPeakMatch> {

    static final Logger log = LogManager.getLogger(G2PEnterPeakMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PEnterPeakMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.peakManager.deal().G2PEnterPeakMatch(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PEnterPeakMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
