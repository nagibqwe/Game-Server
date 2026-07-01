package com.game.zone.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.P2GResCrossZoneReadyZone;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //如果是跨服玩家的匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResCrossZoneReadyZone.MsgID.eMsgID_VALUE, clazz = P2GResCrossZoneReadyZone.class)

public class P2GResCrossZoneReadyZoneHandler extends Handler<P2GResCrossZoneReadyZone> {

    static final Logger log = LogManager.getLogger(P2GResCrossZoneReadyZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResCrossZoneReadyZone messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.copyMapManager.manager().onP2GResReadyZone(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResCrossZoneReadyZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
