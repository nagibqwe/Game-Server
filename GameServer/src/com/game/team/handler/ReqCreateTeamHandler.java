package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqCreateTeam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //创建队伍
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCreateTeam.MsgID.eMsgID_VALUE, clazz = ReqCreateTeam.class)

public class ReqCreateTeamHandler extends Handler<ReqCreateTeam> {

    static final Logger log = LogManager.getLogger(ReqCreateTeamHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCreateTeam messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqCreateTeamHandler(player, messInfo.getType(), messInfo.getAutoAccept());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCreateTeamHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
