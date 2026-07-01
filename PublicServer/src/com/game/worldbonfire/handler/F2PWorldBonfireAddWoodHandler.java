package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.F2PWorldBonfireAddWood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共服篝火升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PWorldBonfireAddWood.MsgID.eMsgID_VALUE, clazz = F2PWorldBonfireAddWood.class)

public class F2PWorldBonfireAddWoodHandler extends Handler<F2PWorldBonfireAddWood> {

    static final Logger log = LogManager.getLogger(F2PWorldBonfireAddWoodHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PWorldBonfireAddWood messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onWorldBonfireLevel(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PWorldBonfireAddWoodHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
