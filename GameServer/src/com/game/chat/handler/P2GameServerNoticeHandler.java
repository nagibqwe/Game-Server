package com.game.chat.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.P2GameServerNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //fight服->游戏服 通知信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GameServerNotice.MsgID.eMsgID_VALUE, clazz = P2GameServerNotice.class)

public class P2GameServerNoticeHandler extends Handler<P2GameServerNotice> {

    static final Logger log = LogManager.getLogger(P2GameServerNoticeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GameServerNotice messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.chatManager.deal().P2GameServerNotice( messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GameServerNoticeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
