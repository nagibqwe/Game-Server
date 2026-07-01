package com.game.horse.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqActiveMountEquipSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 激活宠物助战槽位
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActiveMountEquipSlot.MsgID.eMsgID_VALUE, clazz = ReqActiveMountEquipSlot.class)

public class ReqActiveMountEquipSlotHandler extends Handler<ReqActiveMountEquipSlot> {

    static final Logger log = LogManager.getLogger(ReqActiveMountEquipSlotHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActiveMountEquipSlot message) {
        try {
            long start = TimeUtils.Time();

            Manager.horseManager.deal().activeHorseEquip(session.getExecutor(), message.getSlotId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
