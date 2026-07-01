package com.game.register.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqRegainRole;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //恢复角色
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRegainRole.MsgID.eMsgID_VALUE, clazz = ReqRegainRole.class)

public class ReqRegainRoleHandler extends Handler<ReqRegainRole> {

    static final Logger log = LogManager.getLogger(ReqRegainRoleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRegainRole messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext context = mess.getContext();
            Manager.registerManager.deal().OnReqRegainRole(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRegainRoleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
