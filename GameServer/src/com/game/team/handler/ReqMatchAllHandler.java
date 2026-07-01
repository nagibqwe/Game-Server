package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqMatchAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //自动匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMatchAll.MsgID.eMsgID_VALUE, clazz = ReqMatchAll.class)

public class ReqMatchAllHandler extends Handler<ReqMatchAll> {

    static final Logger log = LogManager.getLogger(ReqMatchAllHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMatchAll messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqMatchAllHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMatchAllHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
