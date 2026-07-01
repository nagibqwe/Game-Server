package com.game.task.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.taskMessage.ResTaskList;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 任务列表
 */
public class ResTaskListHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResTaskListHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResTaskList messInfo = (ResTaskList) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.getMainTask().resetTaskInfo(messInfo.getMainTask());
            log.info("ResTaskList>"+ player.getInfo() + "收到任务ID=" + messInfo.getMainTask().getModelId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResTaskListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
