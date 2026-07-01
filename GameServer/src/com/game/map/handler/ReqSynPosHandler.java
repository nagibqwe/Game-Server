package com.game.map.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqSynPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //客户端同步位置
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSynPos.MsgID.eMsgID_VALUE, clazz = ReqSynPos.class)

public class ReqSynPosHandler extends Handler<ReqSynPos> {

    static final Logger log = LogManager.getLogger(ReqSynPosHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSynPos message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.mapManager.deal().OnReqSynPos(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
