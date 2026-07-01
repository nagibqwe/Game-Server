package com.game.platformevaluate.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlatformEvaluateMessage.ReqEvaluate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求评价
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEvaluate.MsgID.eMsgID_VALUE, clazz = ReqEvaluate.class)

public class ReqEvaluateHandler extends Handler<ReqEvaluate> {

    static final Logger log = LogManager.getLogger(ReqEvaluateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEvaluate messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.platformevaluateManager.deal().onReqEvaluate(player, messInfo);
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEvaluateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
