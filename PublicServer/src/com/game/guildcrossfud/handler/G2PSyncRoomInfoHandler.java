package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PSyncRoomInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知公共服更新状态
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PSyncRoomInfo.MsgID.eMsgID_VALUE, clazz = G2PSyncRoomInfo.class)

public class G2PSyncRoomInfoHandler extends Handler<G2PSyncRoomInfo> {

    static final Logger log = LogManager.getLogger(G2PSyncRoomInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PSyncRoomInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();

            Manager.fudManager.deal().G2PSyncRoomInfo(context);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PSyncRoomInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
