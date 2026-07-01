package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqUpdateMainUIGuideID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //更新主界面引导的最新引导ID
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpdateMainUIGuideID.MsgID.eMsgID_VALUE, clazz = ReqUpdateMainUIGuideID.class)

public class ReqUpdateMainUIGuideIDHandler extends Handler<ReqUpdateMainUIGuideID> {

    static final Logger log = LogManager.getLogger(ReqUpdateMainUIGuideIDHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpdateMainUIGuideID messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.loadScript().OnReqUpdateMainUIGuideID(player, messInfo.getGid());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpdateMainUIGuideIDHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
