package com.game.crossrank.handler;
import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage.ReqG2PSyncCrossRankInfo;
import io.netty.channel.ChannelHandlerContext;


/**
* makehandler  v1.9 for netty
*同步玩家排行数据到公共服
*/
@Message(id = ReqG2PSyncCrossRankInfo.MsgID.eMsgID_VALUE, clazz = ReqG2PSyncCrossRankInfo.class)
public class ReqG2PSyncCrossRankInfoHandler extends Handler<ReqG2PSyncCrossRankInfo>{

    private static final Logger log = LogManager.getLogger(ReqG2PSyncCrossRankInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqG2PSyncCrossRankInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossRankManager.deal().onReqG2PSyncCrossRankInfo(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqG2PSyncCrossRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }
    }

}