package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqSaveFightSkill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //保存出站技能
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSaveFightSkill.MsgID.eMsgID_VALUE, clazz = ReqSaveFightSkill.class)

public class ReqSaveFightSkillHandler extends Handler<ReqSaveFightSkill> {

    static final Logger log = LogManager.getLogger(ReqSaveFightSkillHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSaveFightSkill messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onReqSaveFightSkill(player,messInfo.getPlayedSkillStr());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSaveFightSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
