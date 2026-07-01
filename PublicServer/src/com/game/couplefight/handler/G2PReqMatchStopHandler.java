package com.game.couplefight.handler;

import com.game.couplefight.structs.CoupleData;
import com.game.manager.Manager;
import com.game.structs.SessionKey;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqMatchStop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服-申请取消匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqMatchStop.MsgID.eMsgID_VALUE, clazz = G2PReqMatchStop.class)

public class G2PReqMatchStopHandler extends Handler<G2PReqMatchStop> {

    static final Logger log = LogManager.getLogger(G2PReqMatchStopHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqMatchStop messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().matchStop(mess.getContext(), messInfo.getUid());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqMatchStopHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
