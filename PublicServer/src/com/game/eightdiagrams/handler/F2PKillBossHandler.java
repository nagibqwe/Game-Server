package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.F2PKillBoss;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //杀死BOSS
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PKillBoss.MsgID.eMsgID_VALUE, clazz = F2PKillBoss.class)

public class F2PKillBossHandler extends Handler<F2PKillBoss> {

    static final Logger log = LogManager.getLogger(F2PKillBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PKillBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            EightDiagramsManager.getInstance().deal().F2PKillBoss(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PKillBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
