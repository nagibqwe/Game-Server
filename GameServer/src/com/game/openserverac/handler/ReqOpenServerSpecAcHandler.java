package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenServerSpecAc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求活动
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenServerSpecAc.MsgID.eMsgID_VALUE, clazz = ReqOpenServerSpecAc.class)

public class ReqOpenServerSpecAcHandler extends Handler<ReqOpenServerSpecAc> {

    static final Logger log = LogManager.getLogger(ReqOpenServerSpecAcHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenServerSpecAc messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqOpenServerSpecAc(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenServerSpecAcHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
