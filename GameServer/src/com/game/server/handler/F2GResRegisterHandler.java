package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.F2GResRegister;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //fightServer回复game的协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2GResRegister.MsgID.eMsgID_VALUE, clazz = F2GResRegister.class)

public class F2GResRegisterHandler extends Handler<F2GResRegister> {

    static final Logger log = LogManager.getLogger(F2GResRegisterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2GResRegister messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            GameServer.getServerScript().OnF2GResRegister(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GResRegisterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
