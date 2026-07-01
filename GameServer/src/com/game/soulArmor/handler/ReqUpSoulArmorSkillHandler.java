package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqUpSoulArmorSkill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魂甲觉醒
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpSoulArmorSkill.MsgID.eMsgID_VALUE, clazz = ReqUpSoulArmorSkill.class)

public class ReqUpSoulArmorSkillHandler extends Handler<ReqUpSoulArmorSkill> {

    static final Logger log = LogManager.getLogger(ReqUpSoulArmorSkillHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpSoulArmorSkill messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqUpSoulArmorSkill(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpSoulArmorSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
