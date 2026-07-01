package com.game.server.handler;

import com.game.server.SocialServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage;
import game.message.serverMessage.G2SRegisterServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //game注册到Social的协议
 * @Desc TODO Auto Create
 * @Auth Tool
 */

@Message(id = G2SRegisterServer.MsgID.eMsgID_VALUE, clazz = G2SRegisterServer.class)

public class G2SRegisterServerHandler extends Handler<G2SRegisterServer> {

    static final Logger log = LogManager.getLogger(G2SRegisterServerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SRegisterServer messInfo) {
        try {
            long start = TimeUtils.Time();

            serverMessage.gameServerInfo server = messInfo.getServer();
            SocialServer.getInstance().server().register(mess.getContext(), server);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SRegisterServerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
