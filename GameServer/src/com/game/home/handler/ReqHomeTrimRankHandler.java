package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeTrimRank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取家装大赛排名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeTrimRank.MsgID.eMsgID_VALUE, clazz = ReqHomeTrimRank.class)

public class ReqHomeTrimRankHandler extends Handler<ReqHomeTrimRank> {

    static final Logger log = LogManager.getLogger(ReqHomeTrimRankHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeTrimRank messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeTrimRankHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeTrimRankHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
