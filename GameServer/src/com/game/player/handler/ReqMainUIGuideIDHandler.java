package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqMainUIGuideID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求主界面引导的最新引导ID
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMainUIGuideID.MsgID.eMsgID_VALUE, clazz = ReqMainUIGuideID.class)

public class ReqMainUIGuideIDHandler extends Handler<ReqMainUIGuideID> {

    static final Logger log = LogManager.getLogger(ReqMainUIGuideIDHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMainUIGuideID messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.loadScript().OnReqMainUIGuideID(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMainUIGuideIDHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
