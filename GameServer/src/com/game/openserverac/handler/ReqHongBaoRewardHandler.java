package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqHongBaoReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取红包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHongBaoReward.MsgID.eMsgID_VALUE, clazz = ReqHongBaoReward.class)

public class ReqHongBaoRewardHandler extends Handler<ReqHongBaoReward> {

    static final Logger log = LogManager.getLogger(ReqHongBaoRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHongBaoReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().onReqHongBaoReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHongBaoRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
