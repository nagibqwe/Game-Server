package com.game.equip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipStrengthUpLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipStrengthUpLevel.MsgID.eMsgID_VALUE, clazz = ReqEquipStrengthUpLevel.class)

public class ReqEquipStrengthUpLevelHandler extends Handler<ReqEquipStrengthUpLevel> {

    static final Logger log = LogManager.getLogger(ReqEquipStrengthUpLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipStrengthUpLevel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                Manager.equipManager.deal().onReqEquipStrengthUpLevel(message.getType(), player);
            } else {
                log.error("player is null");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
