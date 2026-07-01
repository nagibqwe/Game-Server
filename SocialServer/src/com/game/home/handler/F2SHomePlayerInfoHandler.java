package com.game.home.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.F2SHomePlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步房屋玩家ID
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2SHomePlayerInfo.MsgID.eMsgID_VALUE, clazz = F2SHomePlayerInfo.class)

public class F2SHomePlayerInfoHandler extends Handler<F2SHomePlayerInfo> {

    static final Logger log = LogManager.getLogger(F2SHomePlayerInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2SHomePlayerInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.homeManager.deal().F2SHomePlayerInfo(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2SHomePlayerInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
