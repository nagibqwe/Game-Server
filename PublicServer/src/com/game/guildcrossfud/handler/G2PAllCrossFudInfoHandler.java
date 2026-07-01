package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PAllCrossFudInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服福地数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PAllCrossFudInfo.MsgID.eMsgID_VALUE, clazz = G2PAllCrossFudInfo.class)

public class G2PAllCrossFudInfoHandler extends Handler<G2PAllCrossFudInfo> {

    static final Logger log = LogManager.getLogger(G2PAllCrossFudInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PAllCrossFudInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PAllCrossFudInfo(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PAllCrossFudInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
