package com.game.godweapon.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GodWeaponMessage.ReqGodWeaponLevelUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //神兵组升级
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGodWeaponLevelUp.MsgID.eMsgID_VALUE, clazz = ReqGodWeaponLevelUp.class)

public class ReqGodWeaponLevelUpHandler extends Handler<ReqGodWeaponLevelUp> {

    static final Logger log = LogManager.getLogger(ReqGodWeaponLevelUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGodWeaponLevelUp message) {
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
