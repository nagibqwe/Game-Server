package com.game.alienboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.G2PCrossAlienCity;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取混沌虚空
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossAlienCity.MsgID.eMsgID_VALUE, clazz = G2PCrossAlienCity.class)

public class G2PCrossAlienCityHandler extends Handler<G2PCrossAlienCity> {

    static final Logger log = LogManager.getLogger(G2PCrossAlienCityHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossAlienCity messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.alien().G2PCrossAlienCity(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossAlienCityHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
