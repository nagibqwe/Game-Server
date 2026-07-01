package com.game.register.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqDeleteRole;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //删除角色
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDeleteRole.MsgID.eMsgID_VALUE, clazz = ReqDeleteRole.class)

public class ReqDeleteRoleHandler extends Handler<ReqDeleteRole> {

    static final Logger log = LogManager.getLogger(ReqDeleteRoleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDeleteRole messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.registerManager.deal().OnReqDeleteRole(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDeleteRoleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
