package com.game.eightdiagrams.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.F2PPlayerToBossHurt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家对BOSS伤害
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PPlayerToBossHurt.MsgID.eMsgID_VALUE, clazz = F2PPlayerToBossHurt.class)

public class F2PPlayerToBossHurtHandler extends Handler<F2PPlayerToBossHurt> {

    static final Logger log = LogManager.getLogger(F2PPlayerToBossHurtHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PPlayerToBossHurt messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PPlayerToBossHurtHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
