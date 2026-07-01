package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IDayCheckInScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqDayCheckIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求签到领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDayCheckIn.MsgID.eMsgID_VALUE, clazz = ReqDayCheckIn.class)

public class ReqDayCheckInHandler extends Handler<ReqDayCheckIn> {

    static final Logger log = LogManager.getLogger(ReqDayCheckInHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDayCheckIn messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();

            IDayCheckInScript script = (IDayCheckInScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.DayCheckIn);
            if (script != null)
                script.onReqDayCheckIn(player, messInfo.getCfgID(), messInfo.getTyp());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDayCheckInHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
