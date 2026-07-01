package com.game.horse.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqMountEquipWear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 穿戴装备
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMountEquipWear.MsgID.eMsgID_VALUE, clazz = ReqMountEquipWear.class)

public class ReqMountEquipWearHandler extends Handler<ReqMountEquipWear> {

    static final Logger log = LogManager.getLogger(ReqMountEquipWearHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMountEquipWear message) {
        try {
            long start = TimeUtils.Time();

            Manager.horseManager.deal().wearEquip(session.getExecutor(), message.getEquipId(), message.getAssistantId(), message.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
