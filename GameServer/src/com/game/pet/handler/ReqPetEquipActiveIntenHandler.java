package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipActiveInten;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 激活全身强化效果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipActiveInten.MsgID.eMsgID_VALUE, clazz = ReqPetEquipActiveInten.class)

public class ReqPetEquipActiveIntenHandler extends Handler<ReqPetEquipActiveInten> {

    static final Logger log = LogManager.getLogger(ReqPetEquipActiveIntenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipActiveInten messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().petEquipIntenActive(player, messInfo.getAssistantId(), messInfo.getStrengthActiveId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipActiveIntenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
