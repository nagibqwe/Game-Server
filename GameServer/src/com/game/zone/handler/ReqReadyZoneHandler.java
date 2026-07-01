package com.game.zone.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ReqReadyZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //匹配玩家同意进入
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqReadyZone.MsgID.eMsgID_VALUE, clazz = ReqReadyZone.class)

public class ReqReadyZoneHandler extends Handler<ReqReadyZone> {

    static final Logger log = LogManager.getLogger(ReqReadyZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqReadyZone messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (null == player) {
                return;
            }
            Manager.copyMapManager.manager().onReqReadyZone(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqReadyZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
