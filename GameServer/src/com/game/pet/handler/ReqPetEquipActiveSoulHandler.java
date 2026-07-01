package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipActiveSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 激活全身附魂效果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipActiveSoul.MsgID.eMsgID_VALUE, clazz = ReqPetEquipActiveSoul.class)

public class ReqPetEquipActiveSoulHandler extends Handler<ReqPetEquipActiveSoul> {

    static final Logger log = LogManager.getLogger(ReqPetEquipActiveSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipActiveSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().petEquipSoulActive(player, messInfo.getAssistantId(), messInfo.getSoulActiveId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipActiveSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
