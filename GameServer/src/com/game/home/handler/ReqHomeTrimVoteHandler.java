package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeTrimVote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //投票家园
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeTrimVote.MsgID.eMsgID_VALUE, clazz = ReqHomeTrimVote.class)

public class ReqHomeTrimVoteHandler extends Handler<ReqHomeTrimVote> {

    static final Logger log = LogManager.getLogger(ReqHomeTrimVoteHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeTrimVote messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeTrimVoteHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeTrimVoteHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
