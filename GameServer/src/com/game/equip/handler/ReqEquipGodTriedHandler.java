package com.game.equip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipGodTried;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //装备神炼
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipGodTried.MsgID.eMsgID_VALUE, clazz = ReqEquipGodTried.class)

public class ReqEquipGodTriedHandler extends Handler<ReqEquipGodTried> {

    static final Logger log = LogManager.getLogger(ReqEquipGodTriedHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipGodTried message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
