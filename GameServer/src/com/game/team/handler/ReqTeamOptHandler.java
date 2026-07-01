package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqTeamOpt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //操作队伍
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTeamOpt.MsgID.eMsgID_VALUE, clazz = ReqTeamOpt.class)

public class ReqTeamOptHandler extends Handler<ReqTeamOpt> {

    static final Logger log = LogManager.getLogger(ReqTeamOptHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTeamOpt messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqTeamOptHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTeamOptHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
