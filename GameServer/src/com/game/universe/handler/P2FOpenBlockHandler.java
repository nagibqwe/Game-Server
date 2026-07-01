package com.game.universe.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.P2FOpenBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知战斗服改变阻挡状
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FOpenBlock.MsgID.eMsgID_VALUE, clazz = P2FOpenBlock.class)

public class P2FOpenBlockHandler extends Handler<P2FOpenBlock> {

    static final Logger log = LogManager.getLogger(P2FOpenBlockHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FOpenBlock messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.universeManager.deal().P2FOpenBlock(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FOpenBlockHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
