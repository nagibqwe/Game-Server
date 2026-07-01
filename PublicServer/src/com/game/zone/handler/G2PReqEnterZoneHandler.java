package com.game.zone.handler;

import com.game.zone.manager.ZoneManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.G2PReqEnterZone;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //进入跨服的排队系统
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqEnterZone.MsgID.eMsgID_VALUE, clazz = G2PReqEnterZone.class)

public class G2PReqEnterZoneHandler extends Handler<G2PReqEnterZone> {

    static final Logger log = LogManager.getLogger(G2PReqEnterZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqEnterZone messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            ZoneManager.deal().OnG2PReqEnterZone(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqEnterZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
