package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.ReqWorldBonfireFinger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求划拳
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldBonfireFinger.MsgID.eMsgID_VALUE, clazz = ReqWorldBonfireFinger.class)

public class ReqWorldBonfireFingerHandler extends Handler<ReqWorldBonfireFinger> {

    static final Logger log = LogManager.getLogger(ReqWorldBonfireFingerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldBonfireFinger messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireFingerGuess((Player) mess.getExecutor(), messInfo.getTeamId(), messInfo.getTotal(), messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldBonfireFingerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
