package com.game.backend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.G2PReqCrossRankDataIsReceive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服请求玩家自己的排行数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossRankDataIsReceive.MsgID.eMsgID_VALUE, clazz = G2PReqCrossRankDataIsReceive.class)

public class G2PReqCrossRankDataIsReceiveHandler extends Handler<G2PReqCrossRankDataIsReceive> {

    static final Logger log = LogManager.getLogger(G2PReqCrossRankDataIsReceiveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossRankDataIsReceive messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.activityManager.deal().OnG2PReqCrossRankDataIsReceive(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossRankDataIsReceiveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
