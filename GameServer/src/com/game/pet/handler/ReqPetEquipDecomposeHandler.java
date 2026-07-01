package com.game.pet.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipDecompose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 装备分解 没有对应的res
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipDecompose.MsgID.eMsgID_VALUE, clazz = ReqPetEquipDecompose.class)

public class ReqPetEquipDecomposeHandler extends Handler<ReqPetEquipDecompose> {

    static final Logger log = LogManager.getLogger(ReqPetEquipDecomposeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipDecompose messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.petManager.deal().petEquipDecompose(mess.getExecutor(), messInfo.getEquipIdList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipDecomposeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
