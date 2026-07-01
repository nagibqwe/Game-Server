package com.game.couplefight.handler;

import com.game.couplefight.structs.CoupleData;
import com.game.manager.Manager;
import com.game.structs.SessionKey;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqMatchStart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服-申请匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqMatchStart.MsgID.eMsgID_VALUE, clazz = G2PReqMatchStart.class)

public class G2PReqMatchStartHandler extends Handler<G2PReqMatchStart> {

    static final Logger log = LogManager.getLogger(G2PReqMatchStartHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqMatchStart messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().matchStart(mess.getContext(), messInfo.getMId(), messInfo.getMpower(), messInfo.getWId(), messInfo.getWpower(), messInfo.getCaptainId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqMatchStartHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
