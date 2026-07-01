package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.F2PReqCrossHorseBossDie;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //指定怪物消失需要同步公共服的数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PReqCrossHorseBossDie.MsgID.eMsgID_VALUE, clazz = F2PReqCrossHorseBossDie.class)

public class F2PReqCrossHorseBossDieHandler extends Handler<F2PReqCrossHorseBossDie> {

    static final Logger log = LogManager.getLogger(F2PReqCrossHorseBossDieHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PReqCrossHorseBossDie messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossHorseBossManager.deal().onF2PReqMonsterDie(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PReqCrossHorseBossDieHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
