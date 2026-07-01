package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.S2GEnterHome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //进入房屋
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2GEnterHome.MsgID.eMsgID_VALUE, clazz = S2GEnterHome.class)

public class S2GEnterHomeHandler extends Handler<S2GEnterHome> {

    static final Logger log = LogManager.getLogger(S2GEnterHomeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GEnterHome messInfo) {
        try {
            long start = TimeUtils.Time();
            Player online = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
            HomeManager.getInstance().deal().S2GEnterHome(online, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GEnterHomeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
