package com.game.kaoshangling.handler;

import com.game.kaoshangling.manager.KaoShangLingManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.KaoShangLingMessage.ReqBuySpecailKaoShangLing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //购买高级犒赏古令
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqBuySpecailKaoShangLing.MsgID.eMsgID_VALUE, clazz = ReqBuySpecailKaoShangLing.class)

public class ReqBuySpecailKaoShangLingHandler extends Handler<ReqBuySpecailKaoShangLing> {

    static final Logger log = LogManager.getLogger(ReqBuySpecailKaoShangLingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBuySpecailKaoShangLing message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            KaoShangLingManager.getInstance().deal().reqBuySpecailKaoShangLing(player, message.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
