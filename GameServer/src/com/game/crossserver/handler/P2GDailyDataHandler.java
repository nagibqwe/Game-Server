package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GDailyData;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //返回跨服活动的相关数据 公共服--》游戏服
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GDailyData.MsgID.eMsgID_VALUE, clazz = P2GDailyData.class)

public class P2GDailyDataHandler extends Handler<P2GDailyData> {

    static final Logger log = LogManager.getLogger(P2GDailyDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GDailyData message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().onP2GDailyData(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
