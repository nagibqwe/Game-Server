package com.game.server.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.G2PReqRegister;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //game注册到public的协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqRegister.MsgID.eMsgID_VALUE, clazz = G2PReqRegister.class)

public class G2PReqRegisterHandler extends Handler<G2PReqRegister> {

    static final Logger log = LogManager.getLogger(G2PReqRegisterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqRegister messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PReqRegister(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqRegisterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
