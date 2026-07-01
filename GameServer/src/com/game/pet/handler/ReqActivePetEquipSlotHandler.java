package com.game.pet.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqActivePetEquipSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 激活宠物助战槽位
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActivePetEquipSlot.MsgID.eMsgID_VALUE, clazz = ReqActivePetEquipSlot.class)

public class ReqActivePetEquipSlotHandler extends Handler<ReqActivePetEquipSlot> {

    static final Logger log = LogManager.getLogger(ReqActivePetEquipSlotHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActivePetEquipSlot messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.petManager.deal().activePetEquip(mess.getExecutor(), messInfo.getSlotId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivePetEquipSlotHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
