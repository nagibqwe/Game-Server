package com.game.devilseries.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqEnterDeviBossMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //进入副本
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterDeviBossMap.MsgID.eMsgID_VALUE, clazz = ReqEnterDeviBossMap.class)

public class ReqEnterDeviBossMapHandler extends Handler<ReqEnterDeviBossMap> {

    static final Logger log = LogManager.getLogger(ReqEnterDeviBossMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEnterDeviBossMap messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.devilSeriesManager.getScript().onReqEnterDeviBossMap(player,messInfo.getMapId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterDeviBossMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
