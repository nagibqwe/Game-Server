package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ReqGetShareReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取分享奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetShareReward.MsgID.eMsgID_VALUE, clazz = ReqGetShareReward.class)

public class ReqGetShareRewardHandler extends Handler<ReqGetShareReward> {

    static final Logger log = LogManager.getLogger(ReqGetShareRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetShareReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player == null || message.getShareId() < 1) {
                logger.error("ReqGetShareRewardHandler parameter is null!");
                return;
            }
            Manager.shareManager.deal().onReqGetShareReward(player, message.getShareId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetShareRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
