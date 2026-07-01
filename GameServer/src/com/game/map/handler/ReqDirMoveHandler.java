package com.game.map.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqDirMove;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //方向移动
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDirMove.MsgID.eMsgID_VALUE, clazz = ReqDirMove.class)

public class ReqDirMoveHandler extends Handler<ReqDirMove> {

    static final Logger log = LogManager.getLogger(ReqDirMoveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDirMove message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.mapManager.deal().OnReqDirMove(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
