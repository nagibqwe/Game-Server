package com.game.team.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/8/3 17:23
 */
public class ResDeleteTeamMemberHandler extends Handler {
    private final Logger log = LogManager.getLogger(ResDeleteTeamMemberHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            TeamMessage.ResDeleteTeamMember messInfo = (TeamMessage.ResDeleteTeamMember) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if(player.getId() == messInfo.getRoleId()){
                player.removeTeam();
            }
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResTeamInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}
