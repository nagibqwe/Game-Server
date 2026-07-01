package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.F2PReqCloneMonsterDie;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //指定怪物或者水晶的消失需要同步公共服的数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PReqCloneMonsterDie.MsgID.eMsgID_VALUE, clazz = F2PReqCloneMonsterDie.class)

public class F2PReqCloneMonsterDieHandler extends Handler<F2PReqCloneMonsterDie> {

    static final Logger log = LogManager.getLogger(F2PReqCloneMonsterDieHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PReqCloneMonsterDie messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onF2PReqCloneMonsterDie(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PReqCloneMonsterDieHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
