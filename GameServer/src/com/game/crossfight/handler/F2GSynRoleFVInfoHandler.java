package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.F2GSynRoleFVInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知游戏服同步角色功能变量信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GSynRoleFVInfo.MsgID.eMsgID_VALUE, clazz = F2GSynRoleFVInfo.class)

public class F2GSynRoleFVInfoHandler extends Handler<F2GSynRoleFVInfo> {

    static final Logger log = LogManager.getLogger(F2GSynRoleFVInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GSynRoleFVInfo message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().onF2GSynRoleFVInfo(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GSynRoleFVInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
