package com.game.server.handler;

import com.game.server.struct.SessionAttribute;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.net.Config.ServerEnum;
import game.core.util.TimeUtils;
import game.message.serverMessage.P2SRegisterCallback;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //public回复Social的协议
 * @Desc TODO Auto Create
 * @Auth Tool
 */

@Message(id = P2SRegisterCallback.MsgID.eMsgID_VALUE, clazz = P2SRegisterCallback.class)

public class P2SRegisterCallbackHandler extends Handler<P2SRegisterCallback> {

    static final Logger log = LogManager.getLogger(P2SRegisterCallbackHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2SRegisterCallback messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            context.channel().attr(SessionAttribute.ServerId).set(messInfo.getPublicId());
            context.channel().attr(SessionAttribute.ServerName).set(messInfo.getPublicName());
            context.channel().attr(SessionAttribute.ServerType).set(ServerEnum.PUBLIC_SERVER);

            log.info("公共服注册回调 server={}", messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2SRegisterCallbackHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
