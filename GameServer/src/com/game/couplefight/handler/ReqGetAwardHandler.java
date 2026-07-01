package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqGetAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetAward.MsgID.eMsgID_VALUE, clazz = ReqGetAward.class)

public class ReqGetAwardHandler extends Handler<ReqGetAward> {

    static final Logger log = LogManager.getLogger(ReqGetAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player) mess.getExecutor(), 3, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
