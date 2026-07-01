package com.game.hook.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HookMessage.ReqEndSitDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //结束打坐
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEndSitDown.MsgID.eMsgID_VALUE, clazz = ReqEndSitDown.class)

public class ReqEndSitDownHandler extends Handler<ReqEndSitDown> {

    static final Logger log = LogManager.getLogger(ReqEndSitDownHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEndSitDown message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.playerHookManager.deal().endSitDownHandler(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
