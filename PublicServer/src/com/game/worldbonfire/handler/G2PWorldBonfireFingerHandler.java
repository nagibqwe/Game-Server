package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireFinger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服划拳
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireFinger.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireFinger.class)

public class G2PWorldBonfireFingerHandler extends Handler<G2PWorldBonfireFinger> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireFingerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireFinger messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onWorldBonfireFinger(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireFingerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
