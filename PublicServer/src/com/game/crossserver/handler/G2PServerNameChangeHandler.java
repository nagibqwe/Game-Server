package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PServerNameChange;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知游戏服名字的变更
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PServerNameChange.MsgID.eMsgID_VALUE, clazz = G2PServerNameChange.class)

public class G2PServerNameChangeHandler extends Handler<G2PServerNameChange> {

    static final Logger log = LogManager.getLogger(G2PServerNameChangeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PServerNameChange messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            MainServer.getInstance().gsmanager().OnG2PServerNameChange(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PServerNameChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
