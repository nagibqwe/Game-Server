package com.game.register.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqLoginGame;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 客户端请求登录消息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLoginGame.MsgID.eMsgID_VALUE, clazz = ReqLoginGame.class)

public class ReqLoginGameHandler extends Handler<ReqLoginGame> {

    static final Logger log = LogManager.getLogger(ReqLoginGameHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLoginGame messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.registerManager.deal().OnReqLoginGame(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLoginGameHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
