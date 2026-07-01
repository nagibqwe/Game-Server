package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenSeverRevelPersonReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领奖请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenSeverRevelPersonReward.MsgID.eMsgID_VALUE, clazz = ReqOpenSeverRevelPersonReward.class)

public class ReqOpenSeverRevelPersonRewardHandler extends Handler<ReqOpenSeverRevelPersonReward> {

    static final Logger log = LogManager.getLogger(ReqOpenSeverRevelPersonRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenSeverRevelPersonReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqPersonReward(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenSeverRevelPersonRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
