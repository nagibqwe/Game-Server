package com.game.bravepeak.handler;

import com.game.zone.manager.ZoneManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BravePeakMessage.G2PGetPlayerBravePeakInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Game -> Public 请求获取玩家勇者巅峰信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PGetPlayerBravePeakInfo.MsgID.eMsgID_VALUE, clazz = G2PGetPlayerBravePeakInfo.class)

public class G2PGetPlayerBravePeakInfoHandler extends Handler<G2PGetPlayerBravePeakInfo> {

    static final Logger log = LogManager.getLogger(G2PGetPlayerBravePeakInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PGetPlayerBravePeakInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            ZoneManager.deal().onG2PGetPlayerBravePeakInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PGetPlayerBravePeakInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
