package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqApplyOpt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //操作申请对象
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqApplyOpt.MsgID.eMsgID_VALUE, clazz = ReqApplyOpt.class)

public class ReqApplyOptHandler extends Handler<ReqApplyOpt> {

    static final Logger log = LogManager.getLogger(ReqApplyOptHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqApplyOpt messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqApplyOptHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqApplyOptHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
