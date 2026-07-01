package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqDeleteCommunityLeaveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //社区删除留言数据列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDeleteCommunityLeaveMessage.MsgID.eMsgID_VALUE, clazz = ReqDeleteCommunityLeaveMessage.class)

public class ReqDeleteCommunityLeaveMessageHandler extends Handler<ReqDeleteCommunityLeaveMessage> {

    static final Logger log = LogManager.getLogger(ReqDeleteCommunityLeaveMessageHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDeleteCommunityLeaveMessage messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqDeleteCommunityLeaveMessage(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDeleteCommunityLeaveMessageHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
