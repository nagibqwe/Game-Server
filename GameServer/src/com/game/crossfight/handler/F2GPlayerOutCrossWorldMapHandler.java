package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.F2GPlayerOutCrossWorldMap;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //战斗服通知游戏服，玩家的野图离开去下一个地图
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GPlayerOutCrossWorldMap.MsgID.eMsgID_VALUE, clazz = F2GPlayerOutCrossWorldMap.class)

public class F2GPlayerOutCrossWorldMapHandler extends Handler<F2GPlayerOutCrossWorldMap> {

    static final Logger log = LogManager.getLogger(F2GPlayerOutCrossWorldMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GPlayerOutCrossWorldMap message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnF2GPlayerOutCrossWorldMap(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GPlayerOutCrossWorldMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
