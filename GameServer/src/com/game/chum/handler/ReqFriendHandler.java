package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqFriend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 双向好友挚友信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFriend.MsgID.eMsgID_VALUE, clazz = ReqFriend.class)

public class ReqFriendHandler extends Handler<ReqFriend> {

    static final Logger log = LogManager.getLogger(ReqFriendHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFriend message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqFriend(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFriendHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
