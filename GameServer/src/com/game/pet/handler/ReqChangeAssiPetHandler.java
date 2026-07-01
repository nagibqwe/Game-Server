package com.game.pet.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqChangeAssiPet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 更换助阵宠物
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeAssiPet.MsgID.eMsgID_VALUE, clazz = ReqChangeAssiPet.class)

public class ReqChangeAssiPetHandler extends Handler<ReqChangeAssiPet> {

    static final Logger log = LogManager.getLogger(ReqChangeAssiPetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeAssiPet messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.petManager.deal().changePetAssiant(mess.getExecutor(), messInfo.getAssistantId(), messInfo.getPetModelId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeAssiPetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
