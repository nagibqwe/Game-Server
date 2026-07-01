package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage;
import game.message.ChatMessage.ChatReqCS;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.1 // 客服端发给游戏服务器 CS
 */
@Message(id = ChatMessage.ChatReqCS.MsgID.eMsgID_VALUE, clazz = ChatMessage.ChatReqCS.class)

public class ChatReqCSHandler extends Handler<ChatReqCS> {

    private static final Logger log = LogManager.getLogger(ChatReqCSHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ChatReqCS message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chatManager.deal().OnChatReqCS(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ChatReqCSHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
