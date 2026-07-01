package com.game.statestifle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateStifleMessage.ReqUpEvolveLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进化升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpEvolveLevel.MsgID.eMsgID_VALUE, clazz = ReqUpEvolveLevel.class)

public class ReqUpEvolveLevelHandler extends Handler<ReqUpEvolveLevel> {

    static final Logger log = LogManager.getLogger(ReqUpEvolveLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpEvolveLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.stateStifleManager.deal().onReqUpEvolveLevel(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpEvolveLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
