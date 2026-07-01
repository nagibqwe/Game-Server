package com.game.crossrank.handler;
import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage.ReqG2PCrossRankInfo;
import io.netty.channel.ChannelHandlerContext;


/**
* makehandler  v1.9 for netty
*请求跨服排行信息
*/
@Message(id = ReqG2PCrossRankInfo.MsgID.eMsgID_VALUE, clazz = ReqG2PCrossRankInfo.class)

public class ReqG2PCrossRankInfoHandler extends Handler<ReqG2PCrossRankInfo>{

    private static final Logger log = LogManager.getLogger(ReqG2PCrossRankInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqG2PCrossRankInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossRankManager.deal().onReqG2PCrossRankInfo(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqG2PCrossRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }
    }
}