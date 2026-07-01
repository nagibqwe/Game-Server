package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.P2GResFightServerList;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //public 通知game 战斗服列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResFightServerList.MsgID.eMsgID_VALUE, clazz = P2GResFightServerList.class)

public class P2GResFightServerListHandler extends Handler<P2GResFightServerList> {

    static final Logger log = LogManager.getLogger(P2GResFightServerListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResFightServerList messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            GameServer.getServerScript().OnP2GResFightServerList(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResFightServerListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
