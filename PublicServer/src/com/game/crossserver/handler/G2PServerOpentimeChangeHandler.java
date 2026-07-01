package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PServerOpentimeChange;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知游戏服开服时间的变更  --游戏服没用
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PServerOpentimeChange.MsgID.eMsgID_VALUE, clazz = G2PServerOpentimeChange.class)

public class G2PServerOpentimeChangeHandler extends Handler<G2PServerOpentimeChange> {

    static final Logger log = LogManager.getLogger(G2PServerOpentimeChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PServerOpentimeChange messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PServerOpentimeChange(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PServerOpentimeChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
