package com.game.zone.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ReqTeamAcceptEnterZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服进入需要队伍同意
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTeamAcceptEnterZone.MsgID.eMsgID_VALUE, clazz = ReqTeamAcceptEnterZone.class)

public class ReqTeamAcceptEnterZoneHandler extends Handler<ReqTeamAcceptEnterZone> {

    static final Logger log = LogManager.getLogger(ReqTeamAcceptEnterZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTeamAcceptEnterZone messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTeamAcceptEnterZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
