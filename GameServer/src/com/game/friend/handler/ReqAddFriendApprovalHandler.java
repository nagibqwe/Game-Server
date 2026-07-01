package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.ReqAddFriendApproval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //添加好友 审批结果请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAddFriendApproval.MsgID.eMsgID_VALUE, clazz = ReqAddFriendApproval.class)

public class ReqAddFriendApprovalHandler extends Handler<ReqAddFriendApproval> {

    static final Logger log = LogManager.getLogger(ReqAddFriendApprovalHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAddFriendApproval messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.friendManager.deal().reqAddFriendApproval(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddFriendApprovalHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
