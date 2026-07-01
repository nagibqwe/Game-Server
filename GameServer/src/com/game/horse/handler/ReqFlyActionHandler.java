package com.game.horse.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqFlyAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 起飞
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFlyAction.MsgID.eMsgID_VALUE, clazz = ReqFlyAction.class)

public class ReqFlyActionHandler extends Handler<ReqFlyAction> {

    static final Logger log = LogManager.getLogger(ReqFlyActionHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFlyAction message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                Manager.horseManager.deal().onReqFlyActionHandler(player, message.getFly(), message.getX(), message.getY());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
