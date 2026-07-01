package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqPeakRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求巅峰排行榜
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPeakRankInfo.MsgID.eMsgID_VALUE, clazz = ReqPeakRankInfo.class)

public class ReqPeakRankInfoHandler extends Handler<ReqPeakRankInfo> {

    static final Logger log = LogManager.getLogger(ReqPeakRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPeakRankInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqPeakRankInfo(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPeakRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
