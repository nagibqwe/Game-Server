package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.F2PCrossFudGain;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //福地结算
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PCrossFudGain.MsgID.eMsgID_VALUE, clazz = F2PCrossFudGain.class)

public class F2PCrossFudGainHandler extends Handler<F2PCrossFudGain> {

    static final Logger log = LogManager.getLogger(F2PCrossFudGainHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PCrossFudGain messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().F2PCrossFudGain(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PCrossFudGainHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
