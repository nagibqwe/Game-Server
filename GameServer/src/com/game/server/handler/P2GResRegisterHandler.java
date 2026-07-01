package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.P2GResRegister;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //public回复game的协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResRegister.MsgID.eMsgID_VALUE, clazz = P2GResRegister.class)

public class P2GResRegisterHandler extends Handler<P2GResRegister> {

    static final Logger log = LogManager.getLogger(P2GResRegisterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResRegister messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            GameServer.getServerScript().OnP2GResRegister(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResRegisterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
