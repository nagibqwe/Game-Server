package com.game.crossfight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.ReqOutFightRoom;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //等待匹配的玩家取消了不去了
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOutFightRoom.MsgID.eMsgID_VALUE, clazz = ReqOutFightRoom.class)

public class ReqOutFightRoomHandler extends Handler<ReqOutFightRoom> {

    static final Logger log = LogManager.getLogger(ReqOutFightRoomHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOutFightRoom message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Player player = (Player) session.getExecutor();
            if (player == null) {
                return;
            }

            Manager.crossServerManager.crossFightdeal().OnReqOutFightRoom(context, player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOutFightRoomHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
