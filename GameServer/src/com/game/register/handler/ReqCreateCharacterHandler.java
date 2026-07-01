package com.game.register.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqCreateCharacter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //创建角色
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCreateCharacter.MsgID.eMsgID_VALUE, clazz = ReqCreateCharacter.class)

public class ReqCreateCharacterHandler extends Handler<ReqCreateCharacter> {

    static final Logger log = LogManager.getLogger(ReqCreateCharacterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCreateCharacter messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.registerManager.deal().OnReqCreateCharacter(context, messInfo.getDevice(), messInfo.getPlayerName(), messInfo.getCareer());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCreateCharacterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
