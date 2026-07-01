package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SHomeTrimMatchScore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取家装大赛投票数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SHomeTrimMatchScore.MsgID.eMsgID_VALUE, clazz = G2SHomeTrimMatchScore.class)

public class G2SHomeTrimMatchScoreHandler extends Handler<G2SHomeTrimMatchScore> {

    static final Logger log = LogManager.getLogger(G2SHomeTrimMatchScoreHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SHomeTrimMatchScore messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SHomeTrimMatchScore(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SHomeTrimMatchScoreHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
