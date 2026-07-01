package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.P2FCloseMap;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知战斗服关闭地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FCloseMap.MsgID.eMsgID_VALUE, clazz = P2FCloseMap.class)

public class P2FCloseMapHandler extends Handler<P2FCloseMap> {

    static final Logger log = LogManager.getLogger(P2FCloseMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FCloseMap messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossServerManager.crossFightdeal().closeMap(messInfo.getRoomID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FCloseMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
