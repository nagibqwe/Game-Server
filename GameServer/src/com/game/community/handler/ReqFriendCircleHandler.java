package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求朋友圈数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFriendCircle.MsgID.eMsgID_VALUE, clazz = ReqFriendCircle.class)

public class ReqFriendCircleHandler extends Handler<ReqFriendCircle> {

    static final Logger log = LogManager.getLogger(ReqFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqFriendCircle(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
