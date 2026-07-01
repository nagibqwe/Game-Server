package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqTransport2Leader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //传送到队长
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTransport2Leader.MsgID.eMsgID_VALUE, clazz = ReqTransport2Leader.class)

public class ReqTransport2LeaderHandler extends Handler<ReqTransport2Leader> {

    static final Logger log = LogManager.getLogger(ReqTransport2LeaderHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTransport2Leader messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqTransport2LeaderHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTransport2LeaderHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
