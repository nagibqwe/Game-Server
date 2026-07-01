package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PCrossFudRank;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取福地积分排名
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossFudRank.MsgID.eMsgID_VALUE, clazz = G2PCrossFudRank.class)

public class G2PCrossFudRankHandler extends Handler<G2PCrossFudRank> {

    static final Logger log = LogManager.getLogger(G2PCrossFudRankHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossFudRank messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PCrossFudRank(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossFudRankHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
