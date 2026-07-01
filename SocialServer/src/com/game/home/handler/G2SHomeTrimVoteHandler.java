package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SHomeTrimVote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //投票家园
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SHomeTrimVote.MsgID.eMsgID_VALUE, clazz = G2SHomeTrimVote.class)

public class G2SHomeTrimVoteHandler extends Handler<G2SHomeTrimVote> {

    static final Logger log = LogManager.getLogger(G2SHomeTrimVoteHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SHomeTrimVote messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SHomeTrimVote(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SHomeTrimVoteHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
