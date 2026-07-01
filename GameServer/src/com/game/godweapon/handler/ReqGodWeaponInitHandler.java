package com.game.godweapon.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GodWeaponMessage.ReqGodWeaponInit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求神兵信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGodWeaponInit.MsgID.eMsgID_VALUE, clazz = ReqGodWeaponInit.class)

public class ReqGodWeaponInitHandler extends Handler<ReqGodWeaponInit> {

    static final Logger log = LogManager.getLogger(ReqGodWeaponInitHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGodWeaponInit message) {
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
