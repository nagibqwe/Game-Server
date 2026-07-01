package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ReqChatRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求查看是否有频道
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChatRoom.MsgID.eMsgID_VALUE, clazz = ReqChatRoom.class)

public class ReqChatRoomHandler extends Handler<ReqChatRoom> {

    static final Logger log = LogManager.getLogger(ReqChatRoomHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChatRoom message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chatManager.deal().onReqChatRoom(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChatRoomHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
