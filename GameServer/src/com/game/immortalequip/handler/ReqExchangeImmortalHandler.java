package com.game.immortalequip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ImmortalEquipMessage.ReqExchangeImmortal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //兑换
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqExchangeImmortal.MsgID.eMsgID_VALUE, clazz = ReqExchangeImmortal.class)

public class ReqExchangeImmortalHandler extends Handler<ReqExchangeImmortal> {

    static final Logger log = LogManager.getLogger(ReqExchangeImmortalHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqExchangeImmortal message) {
        try {
            long start = TimeUtils.Time();

            Player player =   (Player)session.getExecutor();
            Manager.immortalEquipManager.manager().ReqExchangeImmortal(player,message.getModelID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
