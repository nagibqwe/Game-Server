package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGrowUpPur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //购买商品
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGrowUpPur.MsgID.eMsgID_VALUE, clazz = ReqGrowUpPur.class)

public class ReqGrowUpPurHandler extends Handler<ReqGrowUpPur> {

    static final Logger log = LogManager.getLogger(ReqGrowUpPurHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGrowUpPur messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().onReqGrowUpPur(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGrowUpPurHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
