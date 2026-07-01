package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PServerWorldLvChange;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共服游戏服世界等级变化
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PServerWorldLvChange.MsgID.eMsgID_VALUE, clazz = G2PServerWorldLvChange.class)

public class G2PServerWorldLvChangeHandler extends Handler<G2PServerWorldLvChange> {

    static final Logger log = LogManager.getLogger(G2PServerWorldLvChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PServerWorldLvChange messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PServerWorldLvChange(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PServerWorldLvChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
