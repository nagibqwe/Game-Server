package com.game.login.handler;

import game.core.message.Message;
import game.core.message.RMessage;
import com.game.login.LoginVerify;
import game.core.command.Handler;
import game.core.util.TimeUtils;
import game.message.LoginMessage.ReqLogin;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler  v1.0
 */
@Message(id = ReqLogin.MsgID.eMsgID_VALUE, clazz = ReqLogin.class)

public class ReqLoginHandler extends Handler<ReqLogin> {

    private static final Logger log = LogManager.getLogger(ReqLoginHandler.class);


    @Override
    public void action(RMessage mess, ReqLogin messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext iosession = mess.getContext();
            LoginVerify.verify(iosession, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                log.error("ReqLoginHandler deal long time:" + dealtime);
            }
        } catch (ClassCastException e) {
            log.error(e, e);
        }

    }
}