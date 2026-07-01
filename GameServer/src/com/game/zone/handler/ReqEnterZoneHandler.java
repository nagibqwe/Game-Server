package com.game.zone.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ReqEnterZone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入副本
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterZone.MsgID.eMsgID_VALUE, clazz = ReqEnterZone.class)

public class ReqEnterZoneHandler extends Handler<ReqEnterZone> {

    static final Logger log = LogManager.getLogger(ReqEnterZoneHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEnterZone messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (null == player) {
                return;
            }
            Manager.copyMapManager.manager().onReqCopyMapEnter(player, messInfo.getModelId(), messInfo.getParam());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterZoneHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
