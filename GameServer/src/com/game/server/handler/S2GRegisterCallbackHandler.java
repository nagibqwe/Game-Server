package com.game.server.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.S2GRegisterCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //Social回复game的协议
 * @Desc TODO Auto Create
 * @Auth Tool
 */

@Message(id = S2GRegisterCallback.MsgID.eMsgID_VALUE, clazz = S2GRegisterCallback.class)

public class S2GRegisterCallbackHandler extends Handler<S2GRegisterCallback> {

    static final Logger log = LogManager.getLogger(S2GRegisterCallbackHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GRegisterCallback messInfo) {
        try {
            long start = TimeUtils.Time();

            log.info("社交服务器注册成功 server={}", messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GRegisterCallbackHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
