package com.game.immortalsoul.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ImmortalSoulMessage.ReqDismountingSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //拆解仙魂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDismountingSoul.MsgID.eMsgID_VALUE, clazz = ReqDismountingSoul.class)

public class ReqDismountingSoulHandler extends Handler<ReqDismountingSoul> {

    static final Logger log = LogManager.getLogger(ReqDismountingSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDismountingSoul messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();
            Manager.immortalSoulManager.manager().onDismountingSoul(player, messInfo.getUid());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDismountingSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
