package com.game.eightdiagrams.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.F2PKillPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //杀死玩家
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PKillPlayer.MsgID.eMsgID_VALUE, clazz = F2PKillPlayer.class)

public class F2PKillPlayerHandler extends Handler<F2PKillPlayer> {

    static final Logger log = LogManager.getLogger(F2PKillPlayerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PKillPlayer messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PKillPlayerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
