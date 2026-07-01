package com.game.statevip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqStateVip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求境界任务
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqStateVip.MsgID.eMsgID_VALUE, clazz = ReqStateVip.class)

public class ReqStateVipHandler extends Handler<ReqStateVip> {

    static final Logger log = LogManager.getLogger(ReqStateVipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqStateVip messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.stateVipManager.deal().reqStateVip(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqStateVipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
