package com.game.soulbeast.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulBeastMessage.ReqAddGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求扩充格子
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAddGrid.MsgID.eMsgID_VALUE, clazz = ReqAddGrid.class)

public class ReqAddGridHandler extends Handler<ReqAddGrid> {

    static final Logger log = LogManager.getLogger(ReqAddGridHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAddGrid messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.soulBeastManager.deal().reqAddGrid(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAddGridHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
