package com.game.huaxinflysword.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HuaxinFlySwordMessage.ReqQuickEarn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求快速收益
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqQuickEarn.MsgID.eMsgID_VALUE, clazz = ReqQuickEarn.class)

public class ReqQuickEarnHandler extends Handler<ReqQuickEarn> {

    static final Logger log = LogManager.getLogger(ReqQuickEarnHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqQuickEarn message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.huaxinFlySwordManager.swordSoulScript().onReqQuickEarn(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
