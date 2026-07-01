package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GBossRefreshTip;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //刷新BOSS提示 -gamerserver
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GBossRefreshTip.MsgID.eMsgID_VALUE, clazz = P2GBossRefreshTip.class)

public class P2GBossRefreshTipHandler extends Handler<P2GBossRefreshTip> {

    static final Logger log = LogManager.getLogger(P2GBossRefreshTipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GBossRefreshTip message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnP2GBossRefreshTip(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
