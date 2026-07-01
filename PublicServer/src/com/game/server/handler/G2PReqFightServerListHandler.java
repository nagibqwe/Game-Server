package com.game.server.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.serverMessage.G2PReqFightServerList;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //game请求public 战斗服列表  ---游戏服没用
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqFightServerList.MsgID.eMsgID_VALUE, clazz = G2PReqFightServerList.class)

public class G2PReqFightServerListHandler extends Handler<G2PReqFightServerList> {

    static final Logger log = LogManager.getLogger(G2PReqFightServerListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqFightServerList messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PReqFightServerList(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqFightServerListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
