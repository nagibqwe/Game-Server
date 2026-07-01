package com.game.crosshorseboss.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.F2PCrossHorseBossCloneOpen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共跨服,BOSS房间已经开启好了
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PCrossHorseBossCloneOpen.MsgID.eMsgID_VALUE, clazz = F2PCrossHorseBossCloneOpen.class)

public class F2PCrossHorseBossCloneOpenHandler extends Handler<F2PCrossHorseBossCloneOpen> {

    static final Logger log = LogManager.getLogger(F2PCrossHorseBossCloneOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PCrossHorseBossCloneOpen messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PCrossHorseBossCloneOpenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
