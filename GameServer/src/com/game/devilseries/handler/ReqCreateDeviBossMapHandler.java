package com.game.devilseries.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqCreateDeviBossMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求开启伏魔团
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCreateDeviBossMap.MsgID.eMsgID_VALUE, clazz = ReqCreateDeviBossMap.class)

public class ReqCreateDeviBossMapHandler extends Handler<ReqCreateDeviBossMap> {

    static final Logger log = LogManager.getLogger(ReqCreateDeviBossMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCreateDeviBossMap messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.devilSeriesManager.getScript().onReqCreateDeviBossMap(player,messInfo.getCloneId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCreateDeviBossMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
