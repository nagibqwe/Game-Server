package com.game.backpack.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.backpackMessage.ReqOpenBagCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求元宝开启格子
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenBagCell.MsgID.eMsgID_VALUE, clazz = ReqOpenBagCell.class)

public class ReqOpenBagCellHandler extends Handler<ReqOpenBagCell> {

    static final Logger log = LogManager.getLogger(ReqOpenBagCellHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenBagCell message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.backpackManager.deal().OnReqOpenBagCell(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenBagCellHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
