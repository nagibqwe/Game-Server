package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGetXMZBReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetXMZBReward.MsgID.eMsgID_VALUE, clazz = ReqGetXMZBReward.class)

public class ReqGetXMZBRewardHandler extends Handler<ReqGetXMZBReward> {

    static final Logger log = LogManager.getLogger(ReqGetXMZBRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetXMZBReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.openServerAcManager.deal().onReqXmzbReward((Player)mess.getExecutor(),messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetXMZBRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
