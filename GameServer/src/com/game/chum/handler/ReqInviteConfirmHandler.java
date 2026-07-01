package com.game.chum.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChumMessage.ReqInviteConfirm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 邀请确认
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqInviteConfirm.MsgID.eMsgID_VALUE, clazz = ReqInviteConfirm.class)

public class ReqInviteConfirmHandler extends Handler<ReqInviteConfirm> {

    static final Logger log = LogManager.getLogger(ReqInviteConfirmHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqInviteConfirm message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.chumManager.getScript().onReqInviteConfirm(player, message.getInviteID(), message.getAgree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqInviteConfirmHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
