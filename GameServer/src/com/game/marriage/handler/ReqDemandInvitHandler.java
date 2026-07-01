package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqDemandInvit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //索要请帖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDemandInvit.MsgID.eMsgID_VALUE, clazz = ReqDemandInvit.class)

public class ReqDemandInvitHandler extends Handler<ReqDemandInvit> {

    static final Logger log = LogManager.getLogger(ReqDemandInvitHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDemandInvit messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqDemandInvit(player, messInfo.getTimeStart());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDemandInvitHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
