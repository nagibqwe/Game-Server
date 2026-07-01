package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqAgreeInvit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同意索要请帖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAgreeInvit.MsgID.eMsgID_VALUE, clazz = ReqAgreeInvit.class)

public class ReqAgreeInvitHandler extends Handler<ReqAgreeInvit> {

    static final Logger log = LogManager.getLogger(ReqAgreeInvitHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAgreeInvit messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.reqAgreeInvit(player, messInfo.getRoleId(), messInfo.getIsAgree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAgreeInvitHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
