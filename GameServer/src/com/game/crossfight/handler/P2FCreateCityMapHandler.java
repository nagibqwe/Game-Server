package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.P2FCreateCityMap;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 * 通知战斗服创建，地图
 */
@Message(id = P2FCreateCityMap.MsgID.eMsgID_VALUE, clazz = P2FCreateCityMap.class)

public class P2FCreateCityMapHandler extends Handler<P2FCreateCityMap> {

    private static final Logger log = LogManager.getLogger(P2FCreateCityMapHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2FCreateCityMap message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().P2FCreateCityMap(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FCreateCityMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}