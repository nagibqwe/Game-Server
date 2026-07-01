package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqAddCommunityLeaveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqAddCommunityLeaveMessage.MsgID.eMsgID_VALUE, clazz = G2SReqAddCommunityLeaveMessage.class)

public class G2SReqAddCommunityLeaveMessageHandler extends Handler<G2SReqAddCommunityLeaveMessage> {

    static final Logger log = LogManager.getLogger(G2SReqAddCommunityLeaveMessageHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqAddCommunityLeaveMessage messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqAddCommunityLeaveMessage(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqAddCommunityLeaveMessageHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
