package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqJoinHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 加入支援
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqJoinHelp.MsgID.eMsgID_VALUE, clazz = ReqJoinHelp.class)

public class ReqJoinHelpHandler extends Handler<ReqJoinHelp> {

    static final Logger log = LogManager.getLogger(ReqJoinHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqJoinHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.addCommand(new JoinHelpHandler((Player) mess.getExecutor(), messInfo.getId()));

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqJoinHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
