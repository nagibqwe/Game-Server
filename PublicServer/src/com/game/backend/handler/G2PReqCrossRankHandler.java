package com.game.backend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.G2PReqCrossRank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //主要处理跨服活动排行
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossRank.MsgID.eMsgID_VALUE, clazz = G2PReqCrossRank.class)

public class G2PReqCrossRankHandler extends Handler<G2PReqCrossRank> {

    static final Logger log = LogManager.getLogger(G2PReqCrossRankHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossRank messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.activityManager.deal().OnG2PReqCrossRank(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossRankHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
