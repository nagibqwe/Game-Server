package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCompound.MsgID.eMsgID_VALUE, clazz = ReqCompound.class)

public class ReqCompoundHandler extends Handler<ReqCompound> {

    static final Logger log = LogManager.getLogger(ReqCompoundHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCompound message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().OnReqCompound(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCompoundHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
