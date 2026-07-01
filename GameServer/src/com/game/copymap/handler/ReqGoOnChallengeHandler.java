package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqGoOnChallenge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //挑战副本内请求刷怪
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGoOnChallenge.MsgID.eMsgID_VALUE, clazz = ReqGoOnChallenge.class)

public class ReqGoOnChallengeHandler extends Handler<ReqGoOnChallenge> {

    static final Logger log = LogManager.getLogger(ReqGoOnChallengeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGoOnChallenge message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.copyMapManager.singleTower().goOnChallenge(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGoOnChallengeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
