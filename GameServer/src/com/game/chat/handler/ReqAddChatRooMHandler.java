package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ReqAddChatRooM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //加入语音频道
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAddChatRooM.MsgID.eMsgID_VALUE, clazz = ReqAddChatRooM.class)

public class ReqAddChatRooMHandler extends Handler<ReqAddChatRooM> {

    static final Logger log = LogManager.getLogger(ReqAddChatRooMHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAddChatRooM message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chatManager.deal().onRedAddChatRoom(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddChatRooMHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
