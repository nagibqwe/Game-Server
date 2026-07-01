package com.game.activity.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ActivityMessage.ReqActivityDeal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求操作运营活动
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActivityDeal.MsgID.eMsgID_VALUE, clazz = ReqActivityDeal.class)

public class ReqActivityDealHandler extends Handler<ReqActivityDeal> {

    static final Logger log = LogManager.getLogger(ReqActivityDealHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActivityDeal message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.activityManager.deal().onReqActivityDeal(player, message.getType(), message.getData());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivityDealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
