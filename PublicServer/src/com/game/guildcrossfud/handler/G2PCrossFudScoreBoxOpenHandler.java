package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PCrossFudScoreBoxOpen;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取积分宝箱奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossFudScoreBoxOpen.MsgID.eMsgID_VALUE, clazz = G2PCrossFudScoreBoxOpen.class)

public class G2PCrossFudScoreBoxOpenHandler extends Handler<G2PCrossFudScoreBoxOpen> {

    static final Logger log = LogManager.getLogger(G2PCrossFudScoreBoxOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossFudScoreBoxOpen messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PCrossFudScoreBoxOpen(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossFudScoreBoxOpenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
