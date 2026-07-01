package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.F2PSendOverCityInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PSendOverCityInfo.MsgID.eMsgID_VALUE, clazz = F2PSendOverCityInfo.class)

public class F2PSendOverCityInfoHandler extends Handler<F2PSendOverCityInfo> {

    static final Logger log = LogManager.getLogger(F2PSendOverCityInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PSendOverCityInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            EightDiagramsManager.getInstance().deal().F2PSendOverCityInfo(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PSendOverCityInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
