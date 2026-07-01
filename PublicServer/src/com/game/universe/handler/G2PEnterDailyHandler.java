package com.game.universe.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.G2PEnterDaily;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入活动  游戏服--》公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PEnterDaily.MsgID.eMsgID_VALUE, clazz = G2PEnterDaily.class)

public class G2PEnterDailyHandler extends Handler<G2PEnterDaily> {

    static final Logger log = LogManager.getLogger(G2PEnterDailyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PEnterDaily messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.universeManager.manager().enterDaily(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PEnterDailyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
