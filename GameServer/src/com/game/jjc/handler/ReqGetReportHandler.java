package com.game.jjc.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.JJCMessage.ReqGetReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取战报
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetReport.MsgID.eMsgID_VALUE, clazz = ReqGetReport.class)

public class ReqGetReportHandler extends Handler<ReqGetReport> {

    static final Logger log = LogManager.getLogger(ReqGetReportHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetReport message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.jjcManager.deal().OnReqGetReport(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
