package com.game.task.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.taskMessage.ResMainTaskChange;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 主线任务变更
 */
public class ResMainTaskChangeHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResMainTaskChangeHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMainTaskChange messInfo = (ResMainTaskChange) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            Manager.taskManager.mainTask().mainTaskChange(player, messInfo.getMainTask());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMainTaskChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
