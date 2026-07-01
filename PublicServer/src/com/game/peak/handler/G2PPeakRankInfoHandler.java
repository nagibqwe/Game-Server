package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.G2PPeakRankInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服获取排名信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PPeakRankInfo.MsgID.eMsgID_VALUE, clazz = G2PPeakRankInfo.class)

public class G2PPeakRankInfoHandler extends Handler<G2PPeakRankInfo> {

    static final Logger log = LogManager.getLogger(G2PPeakRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PPeakRankInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.peakManager.deal().G2PPeakRankInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PPeakRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
