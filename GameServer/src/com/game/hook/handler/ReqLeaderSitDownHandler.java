package com.game.hook.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HookMessage.ReqLeaderSitDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //传道打坐
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqLeaderSitDown.MsgID.eMsgID_VALUE, clazz = ReqLeaderSitDown.class)

public class ReqLeaderSitDownHandler extends Handler<ReqLeaderSitDown> {

    static final Logger log = LogManager.getLogger(ReqLeaderSitDownHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqLeaderSitDown message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.leaderPreachManager.getScript().onLeaderSitDown(player,message.getIsTrue());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
