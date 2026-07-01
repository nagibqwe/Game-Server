package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.P2FWorldBonfireAddWoodLv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服返回战斗服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FWorldBonfireAddWoodLv.MsgID.eMsgID_VALUE, clazz = P2FWorldBonfireAddWoodLv.class)

public class P2FWorldBonfireAddWoodLvHandler extends Handler<P2FWorldBonfireAddWoodLv> {

    static final Logger log = LogManager.getLogger(P2FWorldBonfireAddWoodLvHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FWorldBonfireAddWoodLv messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireCrossLevel(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FWorldBonfireAddWoodLvHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
