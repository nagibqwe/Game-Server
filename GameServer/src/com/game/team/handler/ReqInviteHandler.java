package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqInvite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //邀请某人加入
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqInvite.MsgID.eMsgID_VALUE, clazz = ReqInvite.class)

public class ReqInviteHandler extends Handler<ReqInvite> {

    static final Logger log = LogManager.getLogger(ReqInviteHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqInvite messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqInviteHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqInviteHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
