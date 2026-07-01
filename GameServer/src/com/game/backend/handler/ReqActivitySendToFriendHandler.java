package com.game.backend.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.ReqActivitySendToFriend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求赠送好友
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActivitySendToFriend.MsgID.eMsgID_VALUE, clazz = ReqActivitySendToFriend.class)

public class ReqActivitySendToFriendHandler extends Handler<ReqActivitySendToFriend> {

    static final Logger log = LogManager.getLogger(ReqActivitySendToFriendHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActivitySendToFriend message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivitySendToFriendHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
