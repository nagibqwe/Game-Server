package com.game.pet.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqPetEquipDecomposeSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 装备自动分解设置保存
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPetEquipDecomposeSetting.MsgID.eMsgID_VALUE, clazz = ReqPetEquipDecomposeSetting.class)

public class ReqPetEquipDecomposeSettingHandler extends Handler<ReqPetEquipDecomposeSetting> {

    static final Logger log = LogManager.getLogger(ReqPetEquipDecomposeSettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPetEquipDecomposeSetting messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.petManager.deal().autoEquipDecomposeSet(mess.getExecutor(), messInfo.getSet());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPetEquipDecomposeSettingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
