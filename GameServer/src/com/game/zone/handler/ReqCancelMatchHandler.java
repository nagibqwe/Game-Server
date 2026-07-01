package com.game.zone.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ReqCancelMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家取消匹配,发送到跨服处理
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCancelMatch.MsgID.eMsgID_VALUE, clazz = ReqCancelMatch.class)

public class ReqCancelMatchHandler extends Handler<ReqCancelMatch> {

    static final Logger log = LogManager.getLogger(ReqCancelMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCancelMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.copyMapManager.manager().onReqCancelMatch(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCancelMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
