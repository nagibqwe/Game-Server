package com.game.soulbeast.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulBeastMessage.ReqSell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //出售协议
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSell.MsgID.eMsgID_VALUE, clazz = ReqSell.class)

public class ReqSellHandler extends Handler<ReqSell> {

    static final Logger log = LogManager.getLogger(ReqSellHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSell messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.soulBeastManager.deal().sellSoulBeastEquipOrItem(player, messInfo.getIdList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSellHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
