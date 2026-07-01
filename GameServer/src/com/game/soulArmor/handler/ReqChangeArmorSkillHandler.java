package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqChangeArmorSkill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //觉醒技能升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeArmorSkill.MsgID.eMsgID_VALUE, clazz = ReqChangeArmorSkill.class)

public class ReqChangeArmorSkillHandler extends Handler<ReqChangeArmorSkill> {

    static final Logger log = LogManager.getLogger(ReqChangeArmorSkillHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeArmorSkill messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqChangeArmorSkill(mess.getExecutor(), messInfo.getSkillId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeArmorSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
