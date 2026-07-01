package com.game.ninedaysfocused.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage.G2PReqApplyNieDaysFocused;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服报名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqApplyNieDaysFocused.MsgID.eMsgID_VALUE, clazz = G2PReqApplyNieDaysFocused.class)

public class G2PReqApplyNieDaysFocusedHandler extends Handler<G2PReqApplyNieDaysFocused> {

    static final Logger log = LogManager.getLogger(G2PReqApplyNieDaysFocusedHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqApplyNieDaysFocused messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqApplyNieDaysFocusedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
