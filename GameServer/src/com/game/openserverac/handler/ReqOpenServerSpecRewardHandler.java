package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenServerSpecReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenServerSpecReward.MsgID.eMsgID_VALUE, clazz = ReqOpenServerSpecReward.class)

public class ReqOpenServerSpecRewardHandler extends Handler<ReqOpenServerSpecReward> {

    static final Logger log = LogManager.getLogger(ReqOpenServerSpecRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenServerSpecReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqOpenServerSpecReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenServerSpecRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
