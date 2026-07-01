package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.structs.SessionKey;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqCouplefightInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求仙侣对决信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCouplefightInfo.MsgID.eMsgID_VALUE, clazz = G2PReqCouplefightInfo.class)

public class G2PReqCouplefightInfoHandler extends Handler<G2PReqCouplefightInfo> {

    static final Logger log = LogManager.getLogger(G2PReqCouplefightInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCouplefightInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqInfo(mess.getContext(), messInfo.getType(), messInfo.getRid(), messInfo.getParamList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCouplefightInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
