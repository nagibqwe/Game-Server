package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryAddFriend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 发展关系
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryAddFriend.MsgID.eMsgID_VALUE, clazz = ReqMarryAddFriend.class)

public class ReqMarryAddFriendHandler extends Handler<ReqMarryAddFriend> {

    static final Logger log = LogManager.getLogger(ReqMarryAddFriendHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryAddFriend messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.wall().reqMarryAddFriend(mess.getExecutor(), messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryAddFriendHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
