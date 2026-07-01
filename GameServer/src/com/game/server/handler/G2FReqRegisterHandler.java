package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.G2FReqRegister;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //game注册到fightServer的协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2FReqRegister.MsgID.eMsgID_VALUE, clazz = G2FReqRegister.class)

public class G2FReqRegisterHandler extends Handler<G2FReqRegister> {

    static final Logger log = LogManager.getLogger(G2FReqRegisterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2FReqRegister messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            GameServer.getServerScript().OnG2FReqRegister(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqRegisterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
