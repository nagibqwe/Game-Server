package com.game.statestifle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateStifleMessage.ReqActiveSoulSpirit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求激活
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveSoulSpirit.MsgID.eMsgID_VALUE, clazz = ReqActiveSoulSpirit.class)

public class ReqActiveSoulSpiritHandler extends Handler<ReqActiveSoulSpirit> {

    static final Logger log = LogManager.getLogger(ReqActiveSoulSpiritHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveSoulSpirit messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.stateStifleManager.deal().onReqActiveSoulSpirit(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveSoulSpiritHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
