package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqUpSoulArmorSlotLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //强化魂印孔位
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpSoulArmorSlotLevel.MsgID.eMsgID_VALUE, clazz = ReqUpSoulArmorSlotLevel.class)

public class ReqUpSoulArmorSlotLevelHandler extends Handler<ReqUpSoulArmorSlotLevel> {

    static final Logger log = LogManager.getLogger(ReqUpSoulArmorSlotLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpSoulArmorSlotLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqUpSoulArmorSlotLevel(mess.getExecutor(), messInfo.getSlotId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpSoulArmorSlotLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
