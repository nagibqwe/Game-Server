package com.game.friend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.G2SReqAddFriendApproval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 游戏服 到 到目标服 审批关系
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqAddFriendApproval.MsgID.eMsgID_VALUE, clazz = G2SReqAddFriendApproval.class)

public class G2SReqAddFriendApprovalHandler extends Handler<G2SReqAddFriendApproval> {

    static final Logger log = LogManager.getLogger(G2SReqAddFriendApprovalHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqAddFriendApproval messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.friendManager.deal().G2SReqAddFriendApproval(messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqAddFriendApprovalHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
