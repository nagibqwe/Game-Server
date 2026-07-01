package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.S2GHomeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步家园等级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2GHomeInfo.MsgID.eMsgID_VALUE, clazz = S2GHomeInfo.class)

public class S2GHomeInfoHandler extends Handler<S2GHomeInfo> {

    static final Logger log = LogManager.getLogger(S2GHomeInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GHomeInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayer(messInfo.getRoleId());
            HomeManager.getInstance().deal().S2GHomeInfo(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GHomeInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
