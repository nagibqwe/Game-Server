package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqMatchConfirm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //匹配确认
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMatchConfirm.MsgID.eMsgID_VALUE, clazz = ReqMatchConfirm.class)

public class ReqMatchConfirmHandler extends Handler<ReqMatchConfirm> {

    static final Logger log = LogManager.getLogger(ReqMatchConfirmHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMatchConfirm messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().matchConfirm((Player)mess.getExecutor(), messInfo.getConfirm());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMatchConfirmHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
