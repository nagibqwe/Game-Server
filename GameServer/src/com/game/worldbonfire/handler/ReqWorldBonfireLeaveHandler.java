package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.ReqWorldBonfireLeave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求离开
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldBonfireLeave.MsgID.eMsgID_VALUE, clazz = ReqWorldBonfireLeave.class)

public class ReqWorldBonfireLeaveHandler extends Handler<ReqWorldBonfireLeave> {

    static final Logger log = LogManager.getLogger(ReqWorldBonfireLeaveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldBonfireLeave messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireFingerLeave((Player) mess.getExecutor(), messInfo.getTeamId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldBonfireLeaveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
