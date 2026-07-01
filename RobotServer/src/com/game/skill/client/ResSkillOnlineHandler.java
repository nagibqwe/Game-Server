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
*上线返回心法列表
*/
public class ResSkillOnlineHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResSkillOnlineHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            SkillMessage.ResSkillOnline messInfo = (SkillMessage.ResSkillOnline) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            Manager.skillManager.deal().initSkillList(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResSkillListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}