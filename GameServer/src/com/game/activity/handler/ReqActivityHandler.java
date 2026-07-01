package com.game.activity.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ActivityMessage.ReqActivity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求运营活动
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActivity.MsgID.eMsgID_VALUE, clazz = ReqActivity.class)

public class ReqActivityHandler extends Handler<ReqActivity> {

    static final Logger log = LogManager.getLogger(ReqActivityHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActivity message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.activityManager.deal().onReqActivity(player, message.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivityHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
