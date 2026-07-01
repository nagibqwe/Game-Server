package com.game.immortalsoul.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ImmortalSoulMessage.ReqUpSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //仙魂升级
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqUpSoul.MsgID.eMsgID_VALUE, clazz = ReqUpSoul.class)

public class ReqUpSoulHandler extends Handler<ReqUpSoul> {

    static final Logger log = LogManager.getLogger(ReqUpSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUpSoul message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.immortalSoulManager.soulLevelUp(player, message.getLocation());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
