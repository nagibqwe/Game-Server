package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqSendFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求发送朋友圈
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendFriendCircle.MsgID.eMsgID_VALUE, clazz = ReqSendFriendCircle.class)

public class ReqSendFriendCircleHandler extends Handler<ReqSendFriendCircle> {

    static final Logger log = LogManager.getLogger(ReqSendFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqSendFriendCircle(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
