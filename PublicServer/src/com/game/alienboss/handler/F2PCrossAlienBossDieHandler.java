package com.game.alienboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.F2PCrossAlienBossDie;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步混沌虚空怪物数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PCrossAlienBossDie.MsgID.eMsgID_VALUE, clazz = F2PCrossAlienBossDie.class)

public class F2PCrossAlienBossDieHandler extends Handler<F2PCrossAlienBossDie> {

    static final Logger log = LogManager.getLogger(F2PCrossAlienBossDieHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PCrossAlienBossDie messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.alien().F2PCrossAlienBossDie(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PCrossAlienBossDieHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
