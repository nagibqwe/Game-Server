package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.F2PResEnterMapSucc;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PResEnterMapSucc.MsgID.eMsgID_VALUE, clazz = F2PResEnterMapSucc.class)

public class F2PResEnterMapSuccHandler extends Handler<F2PResEnterMapSucc> {

    static final Logger log = LogManager.getLogger(F2PResEnterMapSuccHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PResEnterMapSucc messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            EightDiagramsManager.getInstance().deal().F2PResEnterMapSucc(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PResEnterMapSuccHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
