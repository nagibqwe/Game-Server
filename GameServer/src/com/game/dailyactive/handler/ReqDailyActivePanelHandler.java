package com.game.dailyactive.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.ReqDailyActivePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求日常活动信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDailyActivePanel.MsgID.eMsgID_VALUE, clazz = ReqDailyActivePanel.class)

public class ReqDailyActivePanelHandler extends Handler<ReqDailyActivePanel> {

    static final Logger log = LogManager.getLogger(ReqDailyActivePanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDailyActivePanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.dailyActiveManager.deal().sendDailyActivePanelInfo(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
