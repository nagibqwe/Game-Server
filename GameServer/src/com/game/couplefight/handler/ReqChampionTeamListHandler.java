package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionTeamList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求支持战队列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionTeamList.MsgID.eMsgID_VALUE, clazz = ReqChampionTeamList.class)

public class ReqChampionTeamListHandler extends Handler<ReqChampionTeamList> {

    static final Logger log = LogManager.getLogger(ReqChampionTeamListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionTeamList messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player)mess.getExecutor(), 33, messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionTeamListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
