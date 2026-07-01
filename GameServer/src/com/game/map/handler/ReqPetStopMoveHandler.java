package com.game.map.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqPetStopMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 宠物请求停止
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqPetStopMove.MsgID.eMsgID_VALUE, clazz = ReqPetStopMove.class)

public class ReqPetStopMoveHandler extends Handler<ReqPetStopMove> {

    static final Logger log = LogManager.getLogger(ReqPetStopMoveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqPetStopMove message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.mapManager.deal().OnReqPetStopMove(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
