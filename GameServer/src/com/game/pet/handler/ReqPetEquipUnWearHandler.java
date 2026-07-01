package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipUnWear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 卸下装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipUnWear.MsgID.eMsgID_VALUE, clazz = ReqPetEquipUnWear.class)

public class ReqPetEquipUnWearHandler extends Handler<ReqPetEquipUnWear> {

    static final Logger log = LogManager.getLogger(ReqPetEquipUnWearHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipUnWear messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().unwearEquip(player, messInfo.getAssistantId(), messInfo.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipUnWearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
