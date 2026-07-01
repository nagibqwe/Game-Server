package com.game.crossserver.handler;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.script.IGMScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PGMCMD;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服发送GM命令
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PGMCMD.MsgID.eMsgID_VALUE, clazz = G2PGMCMD.class)

public class G2PGMCMDHandler extends Handler<G2PGMCMD> {

    static final Logger log = LogManager.getLogger(G2PGMCMDHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PGMCMD messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GMScript);
            if (is instanceof IGMScript) {
                ((IGMScript) is).OnDeal(context, messInfo);
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PGMCMDHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
