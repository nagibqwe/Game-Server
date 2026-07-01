package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GResCrossDropItem;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //战斗服通知玩家的物品掉落增加
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GResCrossDropItem.MsgID.eMsgID_VALUE, clazz = F2GResCrossDropItem.class)

public class F2GResCrossDropItemHandler extends Handler<F2GResCrossDropItem> {

    static final Logger log = LogManager.getLogger(F2GResCrossDropItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GResCrossDropItem message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GResCrossDropItem(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GResCrossDropItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
