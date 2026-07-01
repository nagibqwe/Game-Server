package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqCommentFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求 评论朋友圈
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCommentFriendCircle.MsgID.eMsgID_VALUE, clazz = ReqCommentFriendCircle.class)

public class ReqCommentFriendCircleHandler extends Handler<ReqCommentFriendCircle> {

    static final Logger log = LogManager.getLogger(ReqCommentFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCommentFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqCommentFriendCircle(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCommentFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
