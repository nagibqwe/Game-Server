package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeTrimMatchScore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取家装大赛投票数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeTrimMatchScore.MsgID.eMsgID_VALUE, clazz = ReqHomeTrimMatchScore.class)

public class ReqHomeTrimMatchScoreHandler extends Handler<ReqHomeTrimMatchScore> {

    static final Logger log = LogManager.getLogger(ReqHomeTrimMatchScoreHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeTrimMatchScore messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeTrimMatchScoreHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeTrimMatchScoreHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
