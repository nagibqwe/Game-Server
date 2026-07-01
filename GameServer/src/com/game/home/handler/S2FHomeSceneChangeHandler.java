package com.game.home.handler;

import com.game.home.manager.HomeManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.S2FHomeSceneChange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //家园扩建或者布置家具
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2FHomeSceneChange.MsgID.eMsgID_VALUE, clazz = S2FHomeSceneChange.class)

public class S2FHomeSceneChangeHandler extends Handler<S2FHomeSceneChange> {

    static final Logger log = LogManager.getLogger(S2FHomeSceneChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2FHomeSceneChange messInfo) {
        try {
            long start = TimeUtils.Time();

            HomeManager.getInstance().deal().doSceneChange(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2FHomeSceneChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
