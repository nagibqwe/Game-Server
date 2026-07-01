package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGrowUpPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取成长之路积分
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGrowUpPoint.MsgID.eMsgID_VALUE, clazz = ReqGrowUpPoint.class)

public class ReqGrowUpPointHandler extends Handler<ReqGrowUpPoint> {

    static final Logger log = LogManager.getLogger(ReqGrowUpPointHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGrowUpPoint messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqGrowUpPoint(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGrowUpPointHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
