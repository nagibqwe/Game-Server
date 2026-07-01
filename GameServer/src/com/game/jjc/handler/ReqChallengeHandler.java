package com.game.jjc.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.JJCMessage.ReqChallenge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //挑战
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChallenge.MsgID.eMsgID_VALUE, clazz = ReqChallenge.class)

public class ReqChallengeHandler extends Handler<ReqChallenge> {

    static final Logger log = LogManager.getLogger(ReqChallengeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChallenge message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.jjcManager.deal().OnReqChallenge(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
