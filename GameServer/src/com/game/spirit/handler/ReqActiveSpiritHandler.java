package com.game.spirit.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SpiritMessage.ReqActiveSpirit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求激活灵体
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveSpirit.MsgID.eMsgID_VALUE, clazz = ReqActiveSpirit.class)

public class ReqActiveSpiritHandler extends Handler<ReqActiveSpirit> {

    static final Logger log = LogManager.getLogger(ReqActiveSpiritHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveSpirit messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.equipManager.deal().activeSpirit(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveSpiritHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
