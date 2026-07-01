package com.game.horse.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqInviteOtherPlayerForRide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求其他的玩家一起同乘多人坐骑一起游玩
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqInviteOtherPlayerForRide.MsgID.eMsgID_VALUE, clazz = ReqInviteOtherPlayerForRide.class)

public class ReqInviteOtherPlayerForRideHandler extends Handler<ReqInviteOtherPlayerForRide> {

    static final Logger log = LogManager.getLogger(ReqInviteOtherPlayerForRideHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqInviteOtherPlayerForRide message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.horseManager.deal().onReqInviteOtherPlayer(player, message.getInvitedPlayerId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
