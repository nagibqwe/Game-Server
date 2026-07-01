package com.game.peak.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PeakMessage.ReqPeakInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求巅峰竞技数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPeakInfo.MsgID.eMsgID_VALUE, clazz = ReqPeakInfo.class)

public class ReqPeakInfoHandler extends Handler<ReqPeakInfo> {

    static final Logger log = LogManager.getLogger(ReqPeakInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPeakInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.peakManager.deal().reqPeakInfo(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPeakInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
