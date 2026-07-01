package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GResCrossUseItem;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //返回通知结果
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GResCrossUseItem.MsgID.eMsgID_VALUE, clazz = F2GResCrossUseItem.class)

public class F2GResCrossUseItemHandler extends Handler<F2GResCrossUseItem> {

    static final Logger log = LogManager.getLogger(F2GResCrossUseItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GResCrossUseItem message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnF2GResCrossUseItem(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GResCrossUseItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
