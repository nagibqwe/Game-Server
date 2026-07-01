package com.game.chat.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.F2GameServerNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //fight服->游戏服 通知信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2GameServerNotice.MsgID.eMsgID_VALUE, clazz = F2GameServerNotice.class)

public class F2GameServerNoticeHandler extends Handler<F2GameServerNotice> {

    static final Logger log = LogManager.getLogger(F2GameServerNoticeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2GameServerNotice messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.chatManager.deal().F2GameServerNotice( messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GameServerNoticeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
