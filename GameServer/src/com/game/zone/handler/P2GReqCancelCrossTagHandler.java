package com.game.zone.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.P2GReqCancelCrossTag;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服发送回来要取消跨服标记
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GReqCancelCrossTag.MsgID.eMsgID_VALUE, clazz = P2GReqCancelCrossTag.class)

public class P2GReqCancelCrossTagHandler extends Handler<P2GReqCancelCrossTag> {

    static final Logger log = LogManager.getLogger(P2GReqCancelCrossTagHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GReqCancelCrossTag messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.copyMapManager.manager().onP2GReqCancelCrossTag(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GReqCancelCrossTagHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
