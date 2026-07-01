package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGetActReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetActReward.MsgID.eMsgID_VALUE, clazz = ReqGetActReward.class)

public class ReqGetActRewardHandler extends Handler<ReqGetActReward> {

    static final Logger log = LogManager.getLogger(ReqGetActRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetActReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().onReqGetActReward(player, messInfo.getType(), messInfo.getCfgId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetActRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
