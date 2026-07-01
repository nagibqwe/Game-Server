package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PDailyData;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服活动的相关数据 游戏服--》公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PDailyData.MsgID.eMsgID_VALUE, clazz = G2PDailyData.class)

public class G2PDailyDataHandler extends Handler<G2PDailyData> {

    static final Logger log = LogManager.getLogger(G2PDailyDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PDailyData messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().onG2PDailyData(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PDailyDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
