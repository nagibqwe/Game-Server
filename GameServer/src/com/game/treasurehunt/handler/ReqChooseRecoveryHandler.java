package com.game.treasurehunt.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntMessage.ReqChooseRecovery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //选择回收
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChooseRecovery.MsgID.eMsgID_VALUE, clazz = ReqChooseRecovery.class)

public class ReqChooseRecoveryHandler extends Handler<ReqChooseRecovery> {

    static final Logger log = LogManager.getLogger(ReqChooseRecoveryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChooseRecovery messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntManager.deal().onReqChooseRecovery(player,messInfo.getItemId(),messInfo.getNum());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChooseRecoveryHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
