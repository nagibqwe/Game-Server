package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.F2PCrossFudInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步福地怪物数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PCrossFudInfo.MsgID.eMsgID_VALUE, clazz = F2PCrossFudInfo.class)

public class F2PCrossFudInfoHandler extends Handler<F2PCrossFudInfo> {

    static final Logger log = LogManager.getLogger(F2PCrossFudInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PCrossFudInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().F2PCrossFudInfo(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PCrossFudInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
