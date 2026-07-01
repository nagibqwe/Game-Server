package com.game.immortalsoul.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ImmortalSoulMessage.ReqInlaySoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //镶嵌仙魂
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqInlaySoul.MsgID.eMsgID_VALUE, clazz = ReqInlaySoul.class)

public class ReqInlaySoulHandler extends Handler<ReqInlaySoul> {

    static final Logger log = LogManager.getLogger(ReqInlaySoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqInlaySoul message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.immortalSoulManager.reqInlaySoul(player, message.getSoulUID(), message.getLocation());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
