package com.game.couplefight.handler;

import com.game.couplefight.structs.CoupleData;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.SessionKey;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqApply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服
* @Desc
* @Auth Tool
*/

@Message(id = G2PReqApply.MsgID.eMsgID_VALUE, clazz = G2PReqApply.class)

public class G2PReqApplyHandler extends Handler<G2PReqApply> {

    static final Logger log = LogManager.getLogger(G2PReqApplyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqApply messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().apply(mess.getContext(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqApplyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
