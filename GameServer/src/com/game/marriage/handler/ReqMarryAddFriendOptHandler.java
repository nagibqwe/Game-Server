package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryAddFriendOpt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 发展关系回应
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryAddFriendOpt.MsgID.eMsgID_VALUE, clazz = ReqMarryAddFriendOpt.class)

public class ReqMarryAddFriendOptHandler extends Handler<ReqMarryAddFriendOpt> {

    static final Logger log = LogManager.getLogger(ReqMarryAddFriendOptHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryAddFriendOpt messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.wall().reqMarryAddFriendOpt(mess.getExecutor(), messInfo.getRoleId(), messInfo.getOpt());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryAddFriendOptHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
