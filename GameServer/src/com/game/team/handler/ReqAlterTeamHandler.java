package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqAlterTeam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //调整队伍信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAlterTeam.MsgID.eMsgID_VALUE, clazz = ReqAlterTeam.class)

public class ReqAlterTeamHandler extends Handler<ReqAlterTeam> {

    static final Logger log = LogManager.getLogger(ReqAlterTeamHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAlterTeam messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqAlterTeamHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAlterTeamHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
