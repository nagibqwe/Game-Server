package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqV4GetAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求领取助力奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqV4GetAward.MsgID.eMsgID_VALUE, clazz = ReqV4GetAward.class)

public class ReqV4GetAwardHandler extends Handler<ReqV4GetAward> {

    static final Logger log = LogManager.getLogger(ReqV4GetAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqV4GetAward messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.v4HelpManager.deal().ReqV4GetAward(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqV4GetAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
