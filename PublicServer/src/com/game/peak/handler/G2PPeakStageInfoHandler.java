package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.G2PPeakStageInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服获取段位信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PPeakStageInfo.MsgID.eMsgID_VALUE, clazz = G2PPeakStageInfo.class)

public class G2PPeakStageInfoHandler extends Handler<G2PPeakStageInfo> {

    static final Logger log = LogManager.getLogger(G2PPeakStageInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PPeakStageInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.peakManager.deal().G2PPeakStageInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PPeakStageInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
