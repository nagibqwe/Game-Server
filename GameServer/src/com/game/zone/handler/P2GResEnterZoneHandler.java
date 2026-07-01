package com.game.zone.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.P2GResEnterZone;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服进行的排队系统反馈
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResEnterZone.MsgID.eMsgID_VALUE, clazz = P2GResEnterZone.class)

public class P2GResEnterZoneHandler extends Handler<P2GResEnterZone> {

    static final Logger log = LogManager.getLogger(P2GResEnterZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResEnterZone messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.copyMapManager.manager().onP2GResEnterZone(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResEnterZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
