package com.game.crossserver.handler;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2FRelive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.6 for netty 玩家复活
 */
@Message(id = CrossServerMessage.G2FRelive.MsgID.eMsgID_VALUE, clazz = CrossServerMessage.G2FRelive.class)
public class G2FReliveHandler extends Handler<G2FRelive> {

    private static final Logger log = LogManager.getLogger(G2FReliveHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, G2FRelive message) {
        try {
            long start = TimeUtils.Time();

            Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).G2FReliveHandler(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReliveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
