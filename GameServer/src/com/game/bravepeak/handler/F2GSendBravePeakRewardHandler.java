package com.game.bravepeak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BravePeakMessage.F2GSendBravePeakReward;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Fight -> Game 跨服发送勇者巅峰的奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GSendBravePeakReward.MsgID.eMsgID_VALUE, clazz = F2GSendBravePeakReward.class)

public class F2GSendBravePeakRewardHandler extends Handler<F2GSendBravePeakReward> {

    static final Logger log = LogManager.getLogger(F2GSendBravePeakRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GSendBravePeakReward message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().onF2GSendBravePeakReward(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GSendBravePeakRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
