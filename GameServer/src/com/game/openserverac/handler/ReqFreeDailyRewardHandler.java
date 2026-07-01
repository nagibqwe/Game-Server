package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqFreeDailyReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求活动预告奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFreeDailyReward.MsgID.eMsgID_VALUE, clazz = ReqFreeDailyReward.class)

public class ReqFreeDailyRewardHandler extends Handler<ReqFreeDailyReward> {

    static final Logger log = LogManager.getLogger(ReqFreeDailyRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFreeDailyReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqFreeDailyReward(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFreeDailyRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
