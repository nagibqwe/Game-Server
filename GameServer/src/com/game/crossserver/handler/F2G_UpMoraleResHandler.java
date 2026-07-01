package com.game.crossserver.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2G_UpMoraleRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服战场鼓舞状态返回
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2G_UpMoraleRes.MsgID.eMsgID_VALUE, clazz = F2G_UpMoraleRes.class)

public class F2G_UpMoraleResHandler extends Handler<F2G_UpMoraleRes> {

    static final Logger log = LogManager.getLogger(F2G_UpMoraleResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2G_UpMoraleRes message) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayerCache(message.getRoleId());

            Manager.copyMapManager.logic().onReqCrossUpMorale(player, message.getDec(), message.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2G_UpMoraleResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
