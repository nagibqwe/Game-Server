package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage;
import game.message.ChatMessage.ChatGetContentCS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * makehandler v1.5 获取语音内容
 */
@Message(id = ChatMessage.ChatGetContentCS.MsgID.eMsgID_VALUE, clazz = ChatMessage.ChatGetContentCS.class)

public class ChatGetContentCSHandler extends Handler<ChatGetContentCS> {

    private static final Logger log = LogManager.getLogger(ChatGetContentCSHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ChatGetContentCS message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chatManager.deal().OnChatGetContentCS(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ChatGetContentCSHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
