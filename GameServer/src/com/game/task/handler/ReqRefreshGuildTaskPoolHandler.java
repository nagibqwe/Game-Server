package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqRefreshGuildTaskPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求刷新公会任务池
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRefreshGuildTaskPool.MsgID.eMsgID_VALUE, clazz = ReqRefreshGuildTaskPool.class)

public class ReqRefreshGuildTaskPoolHandler extends Handler<ReqRefreshGuildTaskPool> {

    static final Logger log = LogManager.getLogger(ReqRefreshGuildTaskPoolHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRefreshGuildTaskPool messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.taskManager.guild().onReqRefreshGuildTaskPool(player,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefreshGuildTaskPoolHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
