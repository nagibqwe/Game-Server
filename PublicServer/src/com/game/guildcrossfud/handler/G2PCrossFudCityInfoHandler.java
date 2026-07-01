package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PCrossFudCityInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取福地详情
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossFudCityInfo.MsgID.eMsgID_VALUE, clazz = G2PCrossFudCityInfo.class)

public class G2PCrossFudCityInfoHandler extends Handler<G2PCrossFudCityInfo> {

    static final Logger log = LogManager.getLogger(G2PCrossFudCityInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossFudCityInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PCrossFudCityInfo(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossFudCityInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
