package com.game.map.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqRelive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 复活
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRelive.MsgID.eMsgID_VALUE, clazz = ReqRelive.class)

public class ReqReliveHandler extends Handler<ReqRelive> {

    static final Logger log = LogManager.getLogger(ReqReliveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRelive message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.mapManager.deal().OnReqRelive(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
