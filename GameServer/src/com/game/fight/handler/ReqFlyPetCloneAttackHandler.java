package com.game.fight.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FightMessage.ReqFlyPetCloneAttack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //飞行坐骑副本的攻击
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFlyPetCloneAttack.MsgID.eMsgID_VALUE, clazz = ReqFlyPetCloneAttack.class)

public class ReqFlyPetCloneAttackHandler extends Handler<ReqFlyPetCloneAttack> {

    static final Logger log = LogManager.getLogger(ReqFlyPetCloneAttackHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFlyPetCloneAttack message) {
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
