package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipStrength;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 强化装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipStrength.MsgID.eMsgID_VALUE, clazz = ReqPetEquipStrength.class)

public class ReqPetEquipStrengthHandler extends Handler<ReqPetEquipStrength> {

    static final Logger log = LogManager.getLogger(ReqPetEquipStrengthHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipStrength messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().intenPetEquip(player, messInfo.getAssistantId(), messInfo.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipStrengthHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
