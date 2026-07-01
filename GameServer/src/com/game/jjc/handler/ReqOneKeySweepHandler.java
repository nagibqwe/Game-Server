package com.game.jjc.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.JJCMessage.ReqOneKeySweep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一键扫荡
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOneKeySweep.MsgID.eMsgID_VALUE, clazz = ReqOneKeySweep.class)

public class ReqOneKeySweepHandler extends Handler<ReqOneKeySweep> {

    static final Logger log = LogManager.getLogger(ReqOneKeySweepHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOneKeySweep messInfo) {
        try {
            long start = TimeUtils.Time();


            Player player = (Player)mess.getExecutor();
            Manager.jjcManager.deal().onReqOneKeySweep(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOneKeySweepHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
