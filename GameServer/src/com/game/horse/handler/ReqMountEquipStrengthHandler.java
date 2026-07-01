package com.game.horse.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqMountEquipStrength;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 强化装备
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMountEquipStrength.MsgID.eMsgID_VALUE, clazz = ReqMountEquipStrength.class)

public class ReqMountEquipStrengthHandler extends Handler<ReqMountEquipStrength> {

    static final Logger log = LogManager.getLogger(ReqMountEquipStrengthHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMountEquipStrength message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.horseManager.deal().intenHorseEquip(player, message.getAssistantId(), message.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
