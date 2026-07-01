package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PCrossFudBoxOpen;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取福地宝箱奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossFudBoxOpen.MsgID.eMsgID_VALUE, clazz = G2PCrossFudBoxOpen.class)

public class G2PCrossFudBoxOpenHandler extends Handler<G2PCrossFudBoxOpen> {

    static final Logger log = LogManager.getLogger(G2PCrossFudBoxOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossFudBoxOpen messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PCrossFudBoxOpen(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossFudBoxOpenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
