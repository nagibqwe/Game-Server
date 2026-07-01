package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireCalcelMatch;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入跨服取消匹配
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireCalcelMatch.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireCalcelMatch.class)

public class G2PWorldBonfireCalcelMatchHandler extends Handler<G2PWorldBonfireCalcelMatch> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireCalcelMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireCalcelMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldBonfireManager.manager().onWorldBonfireCancelMatch(context, messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireCalcelMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
