package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenServerRevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenServerRevel.MsgID.eMsgID_VALUE, clazz = ReqOpenServerRevel.class)

public class ReqOpenServerRevelHandler extends Handler<ReqOpenServerRevel> {

    static final Logger log = LogManager.getLogger(ReqOpenServerRevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenServerRevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.openServerAcManager.deal().initRevel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenServerRevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
