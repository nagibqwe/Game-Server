package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionFansRankList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求粉丝排名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionFansRankList.MsgID.eMsgID_VALUE, clazz = ReqChampionFansRankList.class)

public class ReqChampionFansRankListHandler extends Handler<ReqChampionFansRankList> {

    static final Logger log = LogManager.getLogger(ReqChampionFansRankListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionFansRankList messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player)mess.getExecutor(), 34);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionFansRankListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
