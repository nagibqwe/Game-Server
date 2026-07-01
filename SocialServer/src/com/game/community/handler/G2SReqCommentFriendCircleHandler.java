package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqCommentFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求 评论朋友圈 to 社交服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqCommentFriendCircle.MsgID.eMsgID_VALUE, clazz = G2SReqCommentFriendCircle.class)

public class G2SReqCommentFriendCircleHandler extends Handler<G2SReqCommentFriendCircle> {

    static final Logger log = LogManager.getLogger(G2SReqCommentFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqCommentFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqCommentFriendCircle(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqCommentFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
