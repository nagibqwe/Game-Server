package com.game.crossserver.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PPlayerCareerChange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共服玩家职业的变更
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PPlayerCareerChange.MsgID.eMsgID_VALUE, clazz = G2PPlayerCareerChange.class)

public class G2PPlayerCareerChangeHandler extends Handler<G2PPlayerCareerChange> {

    static final Logger log = LogManager.getLogger(G2PPlayerCareerChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PPlayerCareerChange messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PPlayerCareerChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
