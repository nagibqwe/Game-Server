package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.P2GResFightServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //public 返回game的战斗ID及服务器  ---公共服没用
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResFightServer.MsgID.eMsgID_VALUE, clazz = P2GResFightServer.class)

public class P2GResFightServerHandler extends Handler<P2GResFightServer> {

    static final Logger log = LogManager.getLogger(P2GResFightServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResFightServer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            GameServer.getServerScript().OnP2GResFightServer(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResFightServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
