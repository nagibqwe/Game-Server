package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2PReqOutFightRoom;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知public服务器， 玩家不参加活动了
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqOutFightRoom.MsgID.eMsgID_VALUE, clazz = G2PReqOutFightRoom.class)

public class G2PReqOutFightRoomHandler extends Handler<G2PReqOutFightRoom> {

    static final Logger log = LogManager.getLogger(G2PReqOutFightRoomHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqOutFightRoom messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fightManager.deal().OnG2PReqOutFightRoom(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqOutFightRoomHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
