package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGetRebateBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //---返利宝箱---
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetRebateBox.MsgID.eMsgID_VALUE, clazz = ReqGetRebateBox.class)

public class ReqGetRebateBoxHandler extends Handler<ReqGetRebateBox> {

    static final Logger log = LogManager.getLogger(ReqGetRebateBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetRebateBox messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.openServerAcManager.deal().getRebateBox((Player)mess.getExecutor(), messInfo.getDay());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetRebateBoxHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
