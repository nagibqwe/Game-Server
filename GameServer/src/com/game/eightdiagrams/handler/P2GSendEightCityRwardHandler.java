package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.P2GSendEightCityRward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发送排名奖，各种奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GSendEightCityRward.MsgID.eMsgID_VALUE, clazz = P2GSendEightCityRward.class)

public class P2GSendEightCityRwardHandler extends Handler<P2GSendEightCityRward> {

    static final Logger log = LogManager.getLogger(P2GSendEightCityRwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GSendEightCityRward message) {
        try {
            long start = TimeUtils.Time();

            Manager.eightDiagramsManager.deal().P2GSendEightCityRward(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
