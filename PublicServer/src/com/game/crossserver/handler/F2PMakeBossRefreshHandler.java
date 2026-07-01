package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2PMakeBossRefresh;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //刷新跨服boss
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PMakeBossRefresh.MsgID.eMsgID_VALUE, clazz = F2PMakeBossRefresh.class)

public class F2PMakeBossRefreshHandler extends Handler<F2PMakeBossRefresh> {

    static final Logger log = LogManager.getLogger(F2PMakeBossRefreshHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PMakeBossRefresh messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulAnimalForestManager.manager().onF2PMakeBossRefresh(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PMakeBossRefreshHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
