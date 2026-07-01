package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqDeleteCommunityLeaveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqDeleteCommunityLeaveMessage.MsgID.eMsgID_VALUE, clazz = G2SReqDeleteCommunityLeaveMessage.class)

public class G2SReqDeleteCommunityLeaveMessageHandler extends Handler<G2SReqDeleteCommunityLeaveMessage> {

    static final Logger log = LogManager.getLogger(G2SReqDeleteCommunityLeaveMessageHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqDeleteCommunityLeaveMessage messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqDeleteCommunityLeaveMessage(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqDeleteCommunityLeaveMessageHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
