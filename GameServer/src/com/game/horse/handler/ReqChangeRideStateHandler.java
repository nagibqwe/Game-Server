package com.game.horse.handler;

import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqChangeRideState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求上下坐骑,只适用于外观改变
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeRideState.MsgID.eMsgID_VALUE, clazz = ReqChangeRideState.class)

public class ReqChangeRideStateHandler extends Handler<ReqChangeRideState> {

    static final Logger log = LogManager.getLogger(ReqChangeRideStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeRideState message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                boolean isSuccess = Manager.horseManager.deal().onReqChangeRideState(player, message.getRideState());
                if ( player.playerCrossData.isToFightServer()) {
                    if (isSuccess) {
                        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), session);
                    }
                }
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
