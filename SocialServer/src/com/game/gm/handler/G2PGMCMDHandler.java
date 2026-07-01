package com.game.gm.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PGMCMD;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/8/6 10:54
 * @Auth ZUncle
 */
@Message(id = G2PGMCMD.MsgID.eMsgID_VALUE, clazz = G2PGMCMD.class)

public class G2PGMCMDHandler extends Handler<G2PGMCMD> {

    static final Logger log = LogManager.getLogger(G2PGMCMDHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2PGMCMD message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.gmManager.deal().cmd(context, message.getRoleId(), message.getCmd());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PGMCMDHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
