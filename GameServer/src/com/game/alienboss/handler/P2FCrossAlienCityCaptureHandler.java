package com.game.alienboss.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.P2FCrossAlienCityCapture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知虚空幻境占领消息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FCrossAlienCityCapture.MsgID.eMsgID_VALUE, clazz = P2FCrossAlienCityCapture.class)

public class P2FCrossAlienCityCaptureHandler extends Handler<P2FCrossAlienCityCapture> {

    static final Logger log = LogManager.getLogger(P2FCrossAlienCityCaptureHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FCrossAlienCityCapture messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FCrossAlienCityCaptureHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
