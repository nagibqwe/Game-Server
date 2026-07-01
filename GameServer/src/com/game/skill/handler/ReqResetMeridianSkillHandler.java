package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqResetMeridianSkill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //重置经脉
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqResetMeridianSkill.MsgID.eMsgID_VALUE, clazz = ReqResetMeridianSkill.class)

public class ReqResetMeridianSkillHandler extends Handler<ReqResetMeridianSkill> {

    static final Logger log = LogManager.getLogger(ReqResetMeridianSkillHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqResetMeridianSkill messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onReqResetMeridianSkill(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqResetMeridianSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
