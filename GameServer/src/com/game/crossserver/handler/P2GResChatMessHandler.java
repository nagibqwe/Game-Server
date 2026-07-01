package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GResChatMess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服聊天
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GResChatMess.MsgID.eMsgID_VALUE, clazz = P2GResChatMess.class)

public class P2GResChatMessHandler extends Handler<P2GResChatMess> {

    static final Logger log = LogManager.getLogger(P2GResChatMessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GResChatMess message) {
        try {
            long start = TimeUtils.Time();

            Manager.chatManager.deal().onP2GResChatMess(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
