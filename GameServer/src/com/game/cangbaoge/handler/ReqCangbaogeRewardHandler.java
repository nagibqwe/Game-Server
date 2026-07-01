package com.game.cangbaoge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CangbaogeMessage.ReqCangbaogeReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领奖
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCangbaogeReward.MsgID.eMsgID_VALUE, clazz = ReqCangbaogeReward.class)

public class ReqCangbaogeRewardHandler extends Handler<ReqCangbaogeReward> {

    static final Logger log = LogManager.getLogger(ReqCangbaogeRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCangbaogeReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.cangbaogeManager.deal().ReqCangbaogeReward(player,message.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCangbaogeRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
