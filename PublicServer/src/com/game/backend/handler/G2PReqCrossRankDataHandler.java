package com.game.backend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.G2PReqCrossRankData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求改活动的排行数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossRankData.MsgID.eMsgID_VALUE, clazz = G2PReqCrossRankData.class)

public class G2PReqCrossRankDataHandler extends Handler<G2PReqCrossRankData> {

    static final Logger log = LogManager.getLogger(G2PReqCrossRankDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossRankData messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.activityManager.deal().OnG2PReqCrossRankData(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossRankDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
