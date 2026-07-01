package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GGMCMDResult;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服发送GM命令
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GGMCMDResult.MsgID.eMsgID_VALUE, clazz = P2GGMCMDResult.class)

public class P2GGMCMDResultHandler extends Handler<P2GGMCMDResult> {

    static final Logger log = LogManager.getLogger(P2GGMCMDResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GGMCMDResult message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.gmCommandManager.getGM().OnPublicGMBack(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
