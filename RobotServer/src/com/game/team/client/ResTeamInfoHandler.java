/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.team.manager.TeamManager;
import com.game.team.structs.Team;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author zhaibiao
 */
public class ResTeamInfoHandler extends Handler{
    private final Logger log = LogManager.getLogger(ResTeamInfoHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            TeamMessage.ResTeamInfo messInfo = (TeamMessage.ResTeamInfo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            TeamManager.getInstance().updateTeam(messInfo.getTeamId(), messInfo.getMembersList());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResTeamInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}
