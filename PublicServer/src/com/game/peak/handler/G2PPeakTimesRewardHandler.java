package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.G2PPeakTimesReward;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服领取场次奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PPeakTimesReward.MsgID.eMsgID_VALUE, clazz = G2PPeakTimesReward.class)

public class G2PPeakTimesRewardHandler extends Handler<G2PPeakTimesReward> {

    static final Logger log = LogManager.getLogger(G2PPeakTimesRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PPeakTimesReward messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.peakManager.deal().G2PPeakTimesReward(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PPeakTimesRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
