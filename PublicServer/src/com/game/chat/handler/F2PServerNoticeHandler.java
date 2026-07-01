package com.game.chat.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.F2PServerNotice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //fight服->游戏服 通知信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PServerNotice.MsgID.eMsgID_VALUE, clazz = F2PServerNotice.class)

public class F2PServerNoticeHandler extends Handler<F2PServerNotice> {

    static final Logger log = LogManager.getLogger(F2PServerNoticeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PServerNotice messInfo) {
        try {
            long start = TimeUtils.Time();

            MainServer.getInstance().gsmanager().F2PServerNotice(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PServerNoticeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
