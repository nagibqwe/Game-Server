package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.ReqWorldBonfireAddWood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求添柴
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldBonfireAddWood.MsgID.eMsgID_VALUE, clazz = ReqWorldBonfireAddWood.class)

public class ReqWorldBonfireAddWoodHandler extends Handler<ReqWorldBonfireAddWood> {

    static final Logger log = LogManager.getLogger(ReqWorldBonfireAddWoodHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldBonfireAddWood messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldBonfireManager.manager().onBonfireLevel((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldBonfireAddWoodHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
