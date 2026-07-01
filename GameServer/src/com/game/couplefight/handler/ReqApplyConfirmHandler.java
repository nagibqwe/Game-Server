package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqApplyConfirm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //报名确认
* @Desc
* @Auth Tool
*/

@Message(id = ReqApplyConfirm.MsgID.eMsgID_VALUE, clazz = ReqApplyConfirm.class)

public class ReqApplyConfirmHandler extends Handler<ReqApplyConfirm> {

    static final Logger log = LogManager.getLogger(ReqApplyConfirmHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqApplyConfirm messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().applyConfirm((Player)mess.getExecutor(), messInfo.getConfirm());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqApplyConfirmHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
