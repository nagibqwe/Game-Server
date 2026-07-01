package com.game.server.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.G2PReqFightServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //game到public 服请求战斗   ----游戏服没用
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqFightServer.MsgID.eMsgID_VALUE, clazz = G2PReqFightServer.class)

public class G2PReqFightServerHandler extends Handler<G2PReqFightServer> {

    static final Logger log = LogManager.getLogger(G2PReqFightServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqFightServer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().deal().OnG2PReqFightServer(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqFightServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
