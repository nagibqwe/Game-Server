package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqCommunityLeaveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求社区留言数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCommunityLeaveMessage.MsgID.eMsgID_VALUE, clazz = ReqCommunityLeaveMessage.class)

public class ReqCommunityLeaveMessageHandler extends Handler<ReqCommunityLeaveMessage> {

    static final Logger log = LogManager.getLogger(ReqCommunityLeaveMessageHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCommunityLeaveMessage messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqCommunityLeaveMessage(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCommunityLeaveMessageHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
