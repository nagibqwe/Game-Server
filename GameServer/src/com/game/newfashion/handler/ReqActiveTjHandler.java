package com.game.newfashion.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NewFashionMessage.ReqActiveTj;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //---图鉴激活
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveTj.MsgID.eMsgID_VALUE, clazz = ReqActiveTj.class)

public class ReqActiveTjHandler extends Handler<ReqActiveTj> {

    static final Logger log = LogManager.getLogger(ReqActiveTjHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveTj messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.newFashionManager.deal().ReqActiveTj(player,messInfo.getTjID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveTjHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
