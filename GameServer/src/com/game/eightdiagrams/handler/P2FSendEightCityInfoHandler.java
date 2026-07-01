package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.P2FSendEightCityInfo;


/**z
* makehandler  v1.9 for netty
*
*/
@Message(id = EightDiagramsMessage.P2FSendEightCityInfo.MsgID.eMsgID_VALUE, clazz = EightDiagramsMessage.P2FSendEightCityInfo.class)
public class P2FSendEightCityInfoHandler extends Handler<P2FSendEightCityInfo>{

    private static final Logger log = LogManager.getLogger(P2FSendEightCityInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, P2FSendEightCityInfo message) {
        try {
            long start = TimeUtils.Time();

            Manager.eightDiagramsManager.deal().P2FSendEightCityInfo(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FSendEightCityInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }

    }
}