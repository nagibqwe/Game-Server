package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求朋友圈数据 to 社交服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqFriendCircle.MsgID.eMsgID_VALUE, clazz = G2SReqFriendCircle.class)

public class G2SReqFriendCircleHandler extends Handler<G2SReqFriendCircle> {

    static final Logger log = LogManager.getLogger(G2SReqFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqFriendCircle(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
