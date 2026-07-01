package com.game.skill.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler  v1.5
 * 更新技能
 */
public class ResUpdateSkillHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResUpdateSkillHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            SkillMessage.ResUpdateSkill messInfo = (SkillMessage.ResUpdateSkill) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            Manager.skillManager.deal().updateSkill(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResUpdateSkillHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}