package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 附魂装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipSoul.MsgID.eMsgID_VALUE, clazz = ReqPetEquipSoul.class)

public class ReqPetEquipSoulHandler extends Handler<ReqPetEquipSoul> {

    static final Logger log = LogManager.getLogger(ReqPetEquipSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().soulPetEquip(player, messInfo.getAssistantId(), messInfo.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
