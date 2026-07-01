package com.game.couplefight.handler;

import com.game.couplefight.structs.CoupleData;
import com.game.manager.Manager;
import com.game.structs.SessionKey;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqMatchConfirm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服-匹配确认
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqMatchConfirm.MsgID.eMsgID_VALUE, clazz = G2PReqMatchConfirm.class)

public class G2PReqMatchConfirmHandler extends Handler<G2PReqMatchConfirm> {

    static final Logger log = LogManager.getLogger(G2PReqMatchConfirmHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqMatchConfirm messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().matchConfirm(mess.getContext(), messInfo.getUid(), messInfo.getConfirm());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqMatchConfirmHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
