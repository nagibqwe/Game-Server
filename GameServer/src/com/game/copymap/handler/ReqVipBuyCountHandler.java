package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqVipBuyCount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买副本次数
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqVipBuyCount.MsgID.eMsgID_VALUE, clazz = ReqVipBuyCount.class)

public class ReqVipBuyCountHandler extends Handler<ReqVipBuyCount> {

    static final Logger log = LogManager.getLogger(ReqVipBuyCountHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqVipBuyCount message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.copyMapManager.logic().vipBuyCount(player, message.getCopyId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipBuyCountHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
