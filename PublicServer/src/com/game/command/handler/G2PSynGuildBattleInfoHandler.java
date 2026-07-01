package com.game.command.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.G2PSynGuildBattleInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步公会战排名信息到公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PSynGuildBattleInfo.MsgID.eMsgID_VALUE, clazz = G2PSynGuildBattleInfo.class)

public class G2PSynGuildBattleInfoHandler extends Handler<G2PSynGuildBattleInfo> {

    static final Logger log = LogManager.getLogger(G2PSynGuildBattleInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PSynGuildBattleInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.universeManager.manager().onG2PSynGuildBattleInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PSynGuildBattleInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
