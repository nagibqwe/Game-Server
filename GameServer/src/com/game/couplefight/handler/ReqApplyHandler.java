package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqApply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //申请报名
* @Desc
* @Auth Tool
*/

@Message(id = ReqApply.MsgID.eMsgID_VALUE, clazz = ReqApply.class)

public class ReqApplyHandler extends Handler<ReqApply> {

    static final Logger log = LogManager.getLogger(ReqApplyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqApply messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.couplefightManager.getScript().apply((Player)mess.getExecutor(), messInfo.getName());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqApplyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
