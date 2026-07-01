package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.F2PSoulAnimalCloneOpen;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共跨服,BOSS房间已经开启好了
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PSoulAnimalCloneOpen.MsgID.eMsgID_VALUE, clazz = F2PSoulAnimalCloneOpen.class)

public class F2PSoulAnimalCloneOpenHandler extends Handler<F2PSoulAnimalCloneOpen> {

    static final Logger log = LogManager.getLogger(F2PSoulAnimalCloneOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PSoulAnimalCloneOpen messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onF2PSoulAnimalCloneOpen(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PSoulAnimalCloneOpenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
