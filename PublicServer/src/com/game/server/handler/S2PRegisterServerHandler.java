package com.game.server.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.S2PRegisterServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Social注册到public的协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2PRegisterServer.MsgID.eMsgID_VALUE, clazz = S2PRegisterServer.class)

public class S2PRegisterServerHandler extends Handler<S2PRegisterServer> {

    static final Logger log = LogManager.getLogger(S2PRegisterServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2PRegisterServer messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().S2PRegisterServerHandler(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2PRegisterServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
