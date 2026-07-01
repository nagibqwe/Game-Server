package com.game.immortalequip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ImmortalEquipMessage.ReqCompoundImmortal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCompoundImmortal.MsgID.eMsgID_VALUE, clazz = ReqCompoundImmortal.class)

public class ReqCompoundImmortalHandler extends Handler<ReqCompoundImmortal> {

    static final Logger log = LogManager.getLogger(ReqCompoundImmortalHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCompoundImmortal message) {
        try {
            long start = TimeUtils.Time();

            Player player =   (Player)session.getExecutor();
            Manager.immortalEquipManager.manager().ReqCompoundImmortal(player,message.getPart());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
