package com.game.jjc.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.JJCMessage.ReqJJCexit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //退出jjc
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqJJCexit.MsgID.eMsgID_VALUE, clazz = ReqJJCexit.class)

public class ReqJJCexitHandler extends Handler<ReqJJCexit> {

    static final Logger log = LogManager.getLogger(ReqJJCexitHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqJJCexit message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.jjcManager.scriptclone().OnReqJJCexit(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
