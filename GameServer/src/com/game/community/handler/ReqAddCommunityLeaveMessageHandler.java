package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqAddCommunityLeaveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求添加社区留言数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAddCommunityLeaveMessage.MsgID.eMsgID_VALUE, clazz = ReqAddCommunityLeaveMessage.class)

public class ReqAddCommunityLeaveMessageHandler extends Handler<ReqAddCommunityLeaveMessage> {

    static final Logger log = LogManager.getLogger(ReqAddCommunityLeaveMessageHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAddCommunityLeaveMessage messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqAddCommunityLeaveMessage(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddCommunityLeaveMessageHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
