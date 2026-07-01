package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqBuyBossStateCount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买境界boss次数
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBuyBossStateCount.MsgID.eMsgID_VALUE, clazz = ReqBuyBossStateCount.class)

public class ReqBuyBossStateCountHandler extends Handler<ReqBuyBossStateCount> {

    static final Logger log = LogManager.getLogger(ReqBuyBossStateCountHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBuyBossStateCount message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.stateBoss().doBuyBossCount(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBuyBossStateCountHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
