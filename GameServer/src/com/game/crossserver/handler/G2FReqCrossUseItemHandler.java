package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FReqCrossUseItem;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * makehandler v1.6 for netty 通知战斗服玩家使用了某一个物品及数量
 */
@Message(id = CrossServerMessage.G2FReqCrossUseItem.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FReqCrossUseItem.class)
public class G2FReqCrossUseItemHandler extends Handler<G2FReqCrossUseItem> {

    private static final Logger log = LogManager.getLogger(G2FReqCrossUseItemHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FReqCrossUseItem message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnG2FReqCrossUseItem(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqCrossUseItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
