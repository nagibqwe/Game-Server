package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.ReqWorldBonfireCancelMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求取消匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldBonfireCancelMatch.MsgID.eMsgID_VALUE, clazz = ReqWorldBonfireCancelMatch.class)

public class ReqWorldBonfireCancelMatchHandler extends Handler<ReqWorldBonfireCancelMatch> {

    static final Logger log = LogManager.getLogger(ReqWorldBonfireCancelMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldBonfireCancelMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireFingerCancelMatch((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldBonfireCancelMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
