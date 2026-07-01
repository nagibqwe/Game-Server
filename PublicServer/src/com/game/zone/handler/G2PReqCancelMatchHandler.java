package com.game.zone.handler;

import com.game.zone.manager.ZoneManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.G2PReqCancelMatch;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //取消的玩家，发送回来队伍一起取消
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCancelMatch.MsgID.eMsgID_VALUE, clazz = G2PReqCancelMatch.class)

public class G2PReqCancelMatchHandler extends Handler<G2PReqCancelMatch> {

    static final Logger log = LogManager.getLogger(G2PReqCancelMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCancelMatch messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            ZoneManager.deal().OnG2PReqCancelMatch(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCancelMatchHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
