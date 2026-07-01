package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.S2GActionTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //任务触发
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2GActionTask.MsgID.eMsgID_VALUE, clazz = S2GActionTask.class)

public class S2GActionTaskHandler extends Handler<S2GActionTask> {

    static final Logger log = LogManager.getLogger(S2GActionTaskHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GActionTask messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
            HomeManager.getInstance().deal().doTaskAction(player, messInfo.getType(), messInfo.getArgsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GActionTaskHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
