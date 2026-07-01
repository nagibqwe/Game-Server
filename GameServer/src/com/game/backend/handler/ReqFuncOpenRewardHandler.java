package com.game.backend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqFuncOpenReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //返回功能开启的玩家达到的条件
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFuncOpenReward.MsgID.eMsgID_VALUE, clazz = ReqFuncOpenReward.class)

public class ReqFuncOpenRewardHandler extends Handler<ReqFuncOpenReward> {

    static final Logger log = LogManager.getLogger(ReqFuncOpenRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFuncOpenReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.controlManager.deal().onFuncReward(player, message.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFuncOpenRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
