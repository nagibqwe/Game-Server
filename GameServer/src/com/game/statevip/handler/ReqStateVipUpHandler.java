package com.game.statevip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqStateVipUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求境界升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqStateVipUp.MsgID.eMsgID_VALUE, clazz = ReqStateVipUp.class)

public class ReqStateVipUpHandler extends Handler<ReqStateVipUp> {

    static final Logger log = LogManager.getLogger(ReqStateVipUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqStateVipUp messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.stateVipManager.deal().reqStateVipUp(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqStateVipUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
