package com.game.treasurehunt.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntMessage.ReqOnekeyRecovery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //一键回收
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOnekeyRecovery.MsgID.eMsgID_VALUE, clazz = ReqOnekeyRecovery.class)

public class ReqOnekeyRecoveryHandler extends Handler<ReqOnekeyRecovery> {

    static final Logger log = LogManager.getLogger(ReqOnekeyRecoveryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOnekeyRecovery messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntManager.deal().onReqOnekeyRecovery(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOnekeyRecoveryHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
