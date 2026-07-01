package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.P2FRepChangeCityMap;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 */
@Message(id = EightDiagramsMessage.P2FRepChangeCityMap.MsgID.eMsgID_VALUE, clazz = EightDiagramsMessage.P2FRepChangeCityMap.class)
public class P2FRepChangeCityMapHandler extends Handler<P2FRepChangeCityMap> {

    private static final Logger log = LogManager.getLogger(P2FRepChangeCityMapHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, P2FRepChangeCityMap message) {
        try {
            long start = TimeUtils.Time();

            Manager.eightDiagramsManager.deal().P2FRepChangeCityMap(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FRepChangeCityMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}