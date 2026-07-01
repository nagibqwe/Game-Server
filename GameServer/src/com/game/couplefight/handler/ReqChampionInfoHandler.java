package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求冠军赛主界面信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionInfo.MsgID.eMsgID_VALUE, clazz = ReqChampionInfo.class)

public class ReqChampionInfoHandler extends Handler<ReqChampionInfo> {

    static final Logger log = LogManager.getLogger(ReqChampionInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player) mess.getExecutor(), 30, messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
