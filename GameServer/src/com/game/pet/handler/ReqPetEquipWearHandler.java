package com.game.pet.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipWear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 穿戴装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipWear.MsgID.eMsgID_VALUE, clazz = ReqPetEquipWear.class)

public class ReqPetEquipWearHandler extends Handler<ReqPetEquipWear> {

    static final Logger log = LogManager.getLogger(ReqPetEquipWearHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipWear messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.petManager.deal().wearEquip(mess.getExecutor(), messInfo.getEquipId(), messInfo.getAssistantId(), messInfo.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipWearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
