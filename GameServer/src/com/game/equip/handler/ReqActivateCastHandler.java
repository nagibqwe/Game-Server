package com.game.equip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqActivateCast;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求激活铸灵部位
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActivateCast.MsgID.eMsgID_VALUE, clazz = ReqActivateCast.class)

public class ReqActivateCastHandler extends Handler<ReqActivateCast> {

    static final Logger log = LogManager.getLogger(ReqActivateCastHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActivateCast message) {
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
