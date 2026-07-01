package com.game.recycle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RecycleMessage.ReqRecycle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 回收物品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRecycle.MsgID.eMsgID_VALUE, clazz = ReqRecycle.class)

public class ReqRecycleHandler extends Handler<ReqRecycle> {

    static final Logger log = LogManager.getLogger(ReqRecycleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRecycle messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.recycleManager.deal().onReqRecycle(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRecycleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
