package com.game.spirit.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SpiritMessage.ReqCollectEquip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //收集装备请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCollectEquip.MsgID.eMsgID_VALUE, clazz = ReqCollectEquip.class)

public class ReqCollectEquipHandler extends Handler<ReqCollectEquip> {

    static final Logger log = LogManager.getLogger(ReqCollectEquipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCollectEquip messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.equipManager.deal().collectEquip(player, messInfo.getId(), messInfo.getEquipId(), 0, messInfo.getInherit());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCollectEquipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
