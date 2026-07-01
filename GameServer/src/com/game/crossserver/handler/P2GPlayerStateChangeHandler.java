package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GPlayerStateChange;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服向游戏服通知玩家当前状态的改变
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GPlayerStateChange.MsgID.eMsgID_VALUE, clazz = P2GPlayerStateChange.class)

public class P2GPlayerStateChangeHandler extends Handler<P2GPlayerStateChange> {

    static final Logger log = LogManager.getLogger(P2GPlayerStateChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GPlayerStateChange message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnP2GPlayerStateChange(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
