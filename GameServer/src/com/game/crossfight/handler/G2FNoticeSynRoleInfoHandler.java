package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FNoticeSynRoleInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * makehandler v1.7 for netty 通知战斗服同步角色信息
 */
@Message(id = G2FNoticeSynRoleInfo.MsgID.eMsgID_VALUE, clazz = G2FNoticeSynRoleInfo.class)

public class G2FNoticeSynRoleInfoHandler extends Handler<G2FNoticeSynRoleInfo> {

    private static final Logger log = LogManager.getLogger(G2FNoticeSynRoleInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FNoticeSynRoleInfo message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnG2FNoticeSynRoleInfo(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FNoticeSynRoleInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
