package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FReqCloneFightInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler  v1.6 for netty
 * 跨服战报的协议请求上传
 */
@Message(id = CrossServerMessage.G2FReqCloneFightInfo.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FReqCloneFightInfo.class)
public class G2FReqCloneFightInfoHandler extends Handler<G2FReqCloneFightInfo> {

    private static final Logger log = LogManager.getLogger(G2FReqCloneFightInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FReqCloneFightInfo message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.getCrossServer().OnG2FReqCloneFightInfoHandler(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqCloneFightInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}