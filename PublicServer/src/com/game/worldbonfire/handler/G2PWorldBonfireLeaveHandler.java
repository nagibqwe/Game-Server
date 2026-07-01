package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireLeave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服离开划拳
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireLeave.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireLeave.class)

public class G2PWorldBonfireLeaveHandler extends Handler<G2PWorldBonfireLeave> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireLeaveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireLeave messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onWorldBonfireFingerLeave(messInfo.getTeamId(), messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireLeaveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
