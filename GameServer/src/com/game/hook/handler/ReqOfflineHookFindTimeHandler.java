package com.game.hook.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HookMessage.ReqOfflineHookFindTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求兑换离线经验
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOfflineHookFindTime.MsgID.eMsgID_VALUE, clazz = ReqOfflineHookFindTime.class)

public class ReqOfflineHookFindTimeHandler extends Handler<ReqOfflineHookFindTime> {

    static final Logger log = LogManager.getLogger(ReqOfflineHookFindTimeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOfflineHookFindTime messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.playerHookManager.deal().onReqOfflineHookFindTime(player,messInfo.getIsfind());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOfflineHookFindTimeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
