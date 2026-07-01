package com.game.zone.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.P2GReqMatchSucceed;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //匹配成功后发送到gameServer中转
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GReqMatchSucceed.MsgID.eMsgID_VALUE, clazz = P2GReqMatchSucceed.class)

public class P2GReqMatchSucceedHandler extends Handler<P2GReqMatchSucceed> {

    static final Logger log = LogManager.getLogger(P2GReqMatchSucceedHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GReqMatchSucceed messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.copyMapManager.manager().onP2GMatchSucceed(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GReqMatchSucceedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
