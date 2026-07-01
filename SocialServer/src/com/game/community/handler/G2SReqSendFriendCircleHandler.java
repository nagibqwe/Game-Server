package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqSendFriendCircle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqSendFriendCircle.MsgID.eMsgID_VALUE, clazz = G2SReqSendFriendCircle.class)

public class G2SReqSendFriendCircleHandler extends Handler<G2SReqSendFriendCircle> {

    static final Logger log = LogManager.getLogger(G2SReqSendFriendCircleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqSendFriendCircle messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqSendFriendCircle(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqSendFriendCircleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
