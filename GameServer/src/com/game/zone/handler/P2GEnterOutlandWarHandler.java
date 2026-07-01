package com.game.zone.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.P2GEnterOutlandWar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //外域进入后房间分配的结果返回， 就不返回正常的结算
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GEnterOutlandWar.MsgID.eMsgID_VALUE, clazz = P2GEnterOutlandWar.class)

public class P2GEnterOutlandWarHandler extends Handler<P2GEnterOutlandWar> {

    static final Logger log = LogManager.getLogger(P2GEnterOutlandWarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GEnterOutlandWar messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GEnterOutlandWarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
