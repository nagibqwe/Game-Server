package com.game.friend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.S2GResAddFriendApproval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 社交服 到 游戏服 添加关系响应
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2GResAddFriendApproval.MsgID.eMsgID_VALUE, clazz = S2GResAddFriendApproval.class)

public class S2GResAddFriendApprovalHandler extends Handler<S2GResAddFriendApproval> {

    static final Logger log = LogManager.getLogger(S2GResAddFriendApprovalHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GResAddFriendApproval messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.friendManager.cross().S2GResAddFriendApproval(messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GResAddFriendApprovalHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
