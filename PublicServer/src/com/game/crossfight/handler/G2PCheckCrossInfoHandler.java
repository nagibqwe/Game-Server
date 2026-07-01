package com.game.crossfight.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2PCheckCrossInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //game到public 请求跨服信息能否断线重连
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCheckCrossInfo.MsgID.eMsgID_VALUE, clazz = G2PCheckCrossInfo.class)

public class G2PCheckCrossInfoHandler extends Handler<G2PCheckCrossInfo> {

    static final Logger log = LogManager.getLogger(G2PCheckCrossInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCheckCrossInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PCheckCrossInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCheckCrossInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
