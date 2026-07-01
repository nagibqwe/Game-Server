package com.game.alienboss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.ReqCrossAlienCity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取混沌虚空
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCrossAlienCity.MsgID.eMsgID_VALUE, clazz = ReqCrossAlienCity.class)

public class ReqCrossAlienCityHandler extends Handler<ReqCrossAlienCity> {

    static final Logger log = LogManager.getLogger(ReqCrossAlienCityHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCrossAlienCity messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            Manager.crossFudManager.deal().reqCrossAlienCityHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCrossAlienCityHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
