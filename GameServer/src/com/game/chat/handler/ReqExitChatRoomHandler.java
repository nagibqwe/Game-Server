package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ReqExitChatRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //退出语言频道
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqExitChatRoom.MsgID.eMsgID_VALUE, clazz = ReqExitChatRoom.class)

public class ReqExitChatRoomHandler extends Handler<ReqExitChatRoom> {

    static final Logger log = LogManager.getLogger(ReqExitChatRoomHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqExitChatRoom message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chatManager.deal().onReqExitChatRoom(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExitChatRoomHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
