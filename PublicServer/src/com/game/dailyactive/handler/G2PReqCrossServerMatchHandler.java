package com.game.dailyactive.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage.G2PReqCrossServerMatch;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服 请求公共服 服务器分组数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossServerMatch.MsgID.eMsgID_VALUE, clazz = G2PReqCrossServerMatch.class)

public class G2PReqCrossServerMatchHandler extends Handler<G2PReqCrossServerMatch> {

    static final Logger log = LogManager.getLogger(G2PReqCrossServerMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossServerMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PReqCrossServerMatch(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossServerMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
