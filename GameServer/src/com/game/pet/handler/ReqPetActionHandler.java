package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求宠物动作(激活、强化、出战、休息)
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetAction.MsgID.eMsgID_VALUE, clazz = ReqPetAction.class)

public class ReqPetActionHandler extends Handler<ReqPetAction> {

    static final Logger log = LogManager.getLogger(ReqPetActionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetAction messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            int act = messInfo.getActType();
            int modelId = messInfo.getModelId();
            Manager.petManager.deal().petAction(player, act, modelId, false);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetActionHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
