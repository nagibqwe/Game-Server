package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PDevilBossList;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魔王缝隙boss列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PDevilBossList.MsgID.eMsgID_VALUE, clazz = G2PDevilBossList.class)

public class G2PDevilBossListHandler extends Handler<G2PDevilBossList> {

    static final Logger log = LogManager.getLogger(G2PDevilBossListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PDevilBossList messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PDevilBossList(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PDevilBossListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
