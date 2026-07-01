package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqGotoNextChallenge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求下一关
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGotoNextChallenge.MsgID.eMsgID_VALUE, clazz = ReqGotoNextChallenge.class)

public class ReqGotoNextChallengeHandler extends Handler<ReqGotoNextChallenge> {

    static final Logger log = LogManager.getLogger(ReqGotoNextChallengeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGotoNextChallenge messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.copyMapManager.singleTower().ReqGotoNextChallenge(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGotoNextChallengeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
