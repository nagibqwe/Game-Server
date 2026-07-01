package com.game.statestifle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateStifleMessage.ReqUpPromoteLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求晋升升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpPromoteLevel.MsgID.eMsgID_VALUE, clazz = ReqUpPromoteLevel.class)

public class ReqUpPromoteLevelHandler extends Handler<ReqUpPromoteLevel> {

    static final Logger log = LogManager.getLogger(ReqUpPromoteLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpPromoteLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.stateStifleManager.deal().onReqUpPromoteLevel(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpPromoteLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
